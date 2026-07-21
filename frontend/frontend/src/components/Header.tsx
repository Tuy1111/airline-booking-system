import React from 'react'
import type { AuthenticatedUser } from '../shared/auth/keycloak'

interface HeaderProps {
  activeTab: 'home' | 'search' | 'bookings' | 'profile' | 'admin'
  setActiveTab: (tab: 'home' | 'search' | 'bookings' | 'profile' | 'admin') => void
  user: AuthenticatedUser | null
  onLogin: () => void
  onLogout: () => void
  onRegister: () => void
  unreadNotificationsCount: number
  onToggleNotifications: () => void
}

const navItems = [
  { id: 'home' as const, label: 'Trang chủ' },
  { id: 'search' as const, label: 'Tìm chuyến bay' },
  { id: 'bookings' as const, label: 'Vé của tôi' },
]

export const Header: React.FC<HeaderProps> = ({
  activeTab,
  setActiveTab,
  user,
  onLogin,
  onLogout,
  onRegister,
  unreadNotificationsCount,
  onToggleNotifications,
}) => {
  const isAdmin = Boolean(
    user &&
      (user.roles?.includes('ADMIN') ||
        user.roles?.includes('admin') ||
        user.roles?.includes('ROLE_ADMIN')),
  )

  return (
    <header className="site-header">
      <button className="brand-mark" type="button" onClick={() => setActiveTab('home')}>
        <span className="brand-symbol" aria-hidden="true">
          <span className="material-symbols-outlined">flight_takeoff</span>
        </span>
        <span className="brand-copy">
          <strong>SkySwift</strong>
          <small>Airlines</small>
        </span>
      </button>

      <nav className="page-nav" aria-label="Điều hướng chính">
        {navItems.map((item) => (
          <button
            key={item.id}
            type="button"
            className={activeTab === item.id ? 'active' : ''}
            aria-current={activeTab === item.id ? 'page' : undefined}
            onClick={() => setActiveTab(item.id)}
          >
            {item.label}
          </button>
        ))}
        {user && (
          <button
            type="button"
            className={activeTab === 'profile' ? 'active' : ''}
            aria-current={activeTab === 'profile' ? 'page' : undefined}
            onClick={() => setActiveTab('profile')}
          >
            Hồ sơ
          </button>
        )}
        {isAdmin && (
          <button
            type="button"
            className={activeTab === 'admin' ? 'active admin-link' : 'admin-link'}
            aria-current={activeTab === 'admin' ? 'page' : undefined}
            onClick={() => setActiveTab('admin')}
          >
            Quản trị
          </button>
        )}
      </nav>

      <div className="header-actions">
        <button
          type="button"
          className="icon-button notification-button"
          onClick={onToggleNotifications}
          aria-label={`Thông báo${unreadNotificationsCount > 0 ? `, ${unreadNotificationsCount} chưa đọc` : ''}`}
        >
          <span className="material-symbols-outlined" aria-hidden="true">notifications</span>
          {unreadNotificationsCount > 0 && <span className="notification-count">{unreadNotificationsCount}</span>}
        </button>

        {user ? (
          <div className="signed-in-actions">
            <button type="button" className="user-chip" onClick={() => setActiveTab('profile')}>
              <span className="user-avatar" aria-hidden="true">{user.fullName?.[0] || 'U'}</span>
              <span className="user-details">
                <strong>{user.fullName}</strong>
                <small>{user.email}</small>
              </span>
            </button>
            <button type="button" className="icon-button" onClick={onLogout} aria-label="Đăng xuất">
              <span className="material-symbols-outlined" aria-hidden="true">logout</span>
            </button>
          </div>
        ) : (
          <div className="auth-actions">
            <button type="button" className="text-button" onClick={onLogin}>Đăng nhập</button>
            <button type="button" className="primary-button compact" onClick={onRegister}>Đăng ký</button>
          </div>
        )}
      </div>
    </header>
  )
}
