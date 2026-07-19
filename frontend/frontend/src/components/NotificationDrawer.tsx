import React from 'react'
import type { NotificationItem } from '../features/notifications/types'

interface NotificationDrawerProps {
  isOpen: boolean
  onClose: () => void
  notifications: NotificationItem[]
  onRefresh: () => void
  formatDateTime: (val: string | null | undefined) => string
}

export const NotificationDrawer: React.FC<NotificationDrawerProps> = ({
  isOpen,
  onClose,
  notifications,
  onRefresh,
  formatDateTime,
}) => {
  if (!isOpen) return null

  return (
    <div className="fixed inset-0 z-50 flex justify-end bg-slate-900/40 backdrop-blur-xs animate-fade-in">
      <div className="bg-white w-full max-w-md h-full shadow-2xl flex flex-col border-l border-slate-200">
        {/* Drawer Header */}
        <div className="p-6 border-b border-slate-100 flex items-center justify-between bg-slate-900 text-white">
          <div className="flex items-center gap-2">
            <span className="material-symbols-outlined text-sky-400">notifications</span>
            <h3 className="font-extrabold text-base">Thông báo hệ thống</h3>
          </div>

          <div className="flex items-center gap-2">
            <button
              onClick={onRefresh}
              className="p-1.5 hover:bg-slate-800 text-slate-300 rounded-lg transition-colors"
              title="Làm mới"
            >
              <span className="material-symbols-outlined text-lg">refresh</span>
            </button>
            <button
              onClick={onClose}
              className="p-1.5 hover:bg-slate-800 text-slate-300 rounded-lg transition-colors"
            >
              <span className="material-symbols-outlined text-lg">close</span>
            </button>
          </div>
        </div>

        {/* Notifications List */}
        <div className="flex-1 overflow-y-auto p-4 space-y-3">
          {notifications.length === 0 ? (
            <div className="text-center py-12 text-slate-400 text-sm">
              Không có thông báo mới
            </div>
          ) : (
            notifications.map((item) => (
              <div
                key={item.id}
                className="bg-slate-50 border border-slate-200 rounded-2xl p-4 hover:border-sky-300 transition-all space-y-2"
              >
                <div className="flex justify-between items-center">
                  <span className="text-[10px] font-bold uppercase tracking-wider text-slate-500 bg-white px-2 py-0.5 rounded-md border border-slate-200 flex items-center gap-1">
                    <span className="material-symbols-outlined text-xs">
                      {item.channel === 'EMAIL'
                        ? 'mail'
                        : item.channel === 'SMS'
                        ? 'sms'
                        : 'notifications'}
                    </span>
                    {item.channel}
                  </span>
                  <span
                    className={`badge ${
                      item.status === 'SENT'
                        ? 'badge-success'
                        : item.status === 'PENDING'
                        ? 'badge-warning'
                        : 'badge-danger'
                    }`}
                  >
                    {item.status}
                  </span>
                </div>

                <div className="text-xs font-bold text-slate-900">{item.templateCode}</div>
                <div className="text-[11px] text-slate-600 font-medium">
                  Người nhận: {item.recipient}
                </div>
                <div className="text-[10px] text-slate-400">
                  {formatDateTime(item.createdAt)}
                </div>
              </div>
            ))
          )}
        </div>
      </div>
    </div>
  )
}
