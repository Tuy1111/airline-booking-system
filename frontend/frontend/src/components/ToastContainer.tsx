import React, { useEffect } from 'react'

export interface ToastMessage {
  id: string
  type: 'success' | 'error' | 'info' | 'warning'
  message: string
}

interface ToastContainerProps {
  toasts: ToastMessage[]
  onDismiss: (id: string) => void
}

export const ToastContainer: React.FC<ToastContainerProps> = ({ toasts, onDismiss }) => {
  return (
    <div className="fixed top-20 right-4 md:right-6 z-[9999] flex flex-col gap-3 max-w-sm w-full pointer-events-none">
      {toasts.map((toast) => (
        <ToastItem key={toast.id} toast={toast} onDismiss={onDismiss} />
      ))}
    </div>
  )
}

const ToastItem: React.FC<{ toast: ToastMessage; onDismiss: (id: string) => void }> = ({
  toast,
  onDismiss,
}) => {
  useEffect(() => {
    const timer = setTimeout(() => {
      onDismiss(toast.id)
    }, 4500)
    return () => clearTimeout(timer)
  }, [toast.id, onDismiss])

  const typeStyles = {
    success: 'bg-emerald-950/95 text-emerald-100 border-emerald-500/40 shadow-emerald-950/40',
    error: 'bg-red-950/95 text-red-100 border-red-500/40 shadow-red-950/40',
    info: 'bg-sky-950/95 text-sky-100 border-sky-500/40 shadow-sky-950/40',
    warning: 'bg-amber-950/95 text-amber-100 border-amber-500/40 shadow-amber-950/40',
  }

  const iconNames = {
    success: 'check_circle',
    error: 'error',
    info: 'info',
    warning: 'warning',
  }

  const iconColors = {
    success: 'text-emerald-400',
    error: 'text-red-400',
    info: 'text-sky-400',
    warning: 'text-amber-400',
  }

  return (
    <div
      className={`pointer-events-auto flex items-start gap-3 p-4 rounded-2xl border backdrop-blur-md shadow-2xl transition-all duration-300 transform animate-fade-in ${typeStyles[toast.type]}`}
    >
      <span className={`material-symbols-outlined text-xl ${iconColors[toast.type]}`}>
        {iconNames[toast.type]}
      </span>
      <div className="flex-1 text-xs font-semibold leading-relaxed break-words">{toast.message}</div>
      <button
        onClick={() => onDismiss(toast.id)}
        className="text-white/60 hover:text-white transition-colors"
      >
        <span className="material-symbols-outlined text-base">close</span>
      </button>
    </div>
  )
}
