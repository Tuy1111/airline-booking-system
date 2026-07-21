import json
import os
import subprocess
import sys


BACKENDS = (
    ("service-registry", "infra/service-registry"),
    ("api-gateway", "infra/api-gateway"),
    ("flight-search-service", "services/flight-search-service"),
    ("booking-service", "services/booking-service"),
    ("user-service", "services/user-service"),
    ("payment-service", "services/payment-service"),
    ("notification-service", "services/notification-service"),
)


def detect(paths, force_all=False):
    selected = set()
    frontend = force_all
    full_deploy = force_all

    for raw_path in paths:
        path = raw_path.replace("\\", "/")

        if path in {"Dockerfile", ".dockerignore", "pom.xml"} or path.startswith("shared/"):
            selected.update(name for name, _ in BACKENDS)

        for name, module in BACKENDS:
            if path.startswith(f"{module}/"):
                selected.add(name)

        if path.startswith("frontend/frontend/"):
            frontend = True
        if path.startswith("infra/caddy/"):
            selected.add("caddy")
        if path.startswith("infra/keycloak/"):
            selected.add("keycloak")
        if path == "docker-compose.prod.yml":
            # ponytail: compose changes deploy everything; refine only if this becomes frequent.
            full_deploy = True

    if force_all:
        selected.update(name for name, _ in BACKENDS)

    matrix = [
        {"name": name, "module": module}
        for name, module in BACKENDS
        if name in selected
    ]
    ordered_services = [name for name, _ in BACKENDS if name in selected]
    if frontend:
        ordered_services.append("frontend")
    ordered_services.extend(name for name in ("caddy", "keycloak") if name in selected)

    return {
        "backend_changed": bool(matrix),
        "backend_matrix": {"include": matrix},
        "frontend": frontend,
        "full_deploy": full_deploy,
        "has_deploy_changes": full_deploy or bool(ordered_services),
        "services": " ".join(ordered_services),
    }


def changed_files(base, head):
    if not base or not head or set(base) == {"0"}:
        return None

    result = subprocess.run(
        ["git", "diff", "--name-only", base, head],
        check=False,
        capture_output=True,
        text=True,
    )
    return result.stdout.splitlines() if result.returncode == 0 else None


def self_test():
    assert detect(["services/booking-service/src/App.java"])["services"] == "booking-service"
    assert len(detect(["shared/common-web/pom.xml"])["backend_matrix"]["include"]) == len(BACKENDS)
    assert detect(["frontend/frontend/src/App.tsx"])["services"] == "frontend"
    assert detect(["infra/caddy/Caddyfile"])["services"] == "caddy"
    assert detect(["README.md"])["has_deploy_changes"] is False
    assert detect(["docker-compose.prod.yml"])["full_deploy"] is True
    print("detect-services self-test passed")


def main():
    if "--self-test" in sys.argv:
        self_test()
        return

    self_test()
    force_all = os.environ.get("GITHUB_EVENT_NAME") == "workflow_dispatch"
    paths = changed_files(os.environ.get("BASE_SHA", ""), os.environ.get("GITHUB_SHA", ""))
    if paths is None:
        force_all = True
        paths = []

    outputs = detect(paths, force_all)
    output_file = os.environ.get("GITHUB_OUTPUT")
    if not output_file:
        raise RuntimeError("GITHUB_OUTPUT is required")

    with open(output_file, "a", encoding="utf-8") as stream:
        for key, value in outputs.items():
            if isinstance(value, bool):
                value = str(value).lower()
            elif isinstance(value, dict):
                value = json.dumps(value, separators=(",", ":"))
            stream.write(f"{key}={value}\n")

    print(f"Changed paths: {len(paths)}")
    print(f"Deploy services: {outputs['services'] or 'none'}")
    print(f"Full deploy: {outputs['full_deploy']}")


if __name__ == "__main__":
    main()
