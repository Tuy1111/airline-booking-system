import React from 'react'
import type { AuthenticatedUser } from '../shared/auth/keycloak'

interface HeaderProps {
  activeTab: 'search' | 'bookings' | 'profile' | 'admin'
  setActiveTab: (tab: 'search' | 'bookings' | 'profile' | 'admin') => void
  user: AuthenticatedUser | null
  onLogin: () => void
  onLogout: () => void
  onRegister: () => void
  unreadNotificationsCount: number
  onToggleNotifications: () => void
  onOpenProfileModal: () => void
}

export const Header: React.FC<HeaderProps> = ({
  activeTab,
  setActiveTab,
  user,
  onLogin,
  onLogout,
  onRegister,
  unreadNotificationsCount,
  onToggleNotifications,
  onOpenProfileModal,
}) => {
  // Only users with ADMIN role in Keycloak can see the Admin button after logging in
  const isAdmin = Boolean(
    user &&
      (user.roles?.includes('ADMIN') ||
        user.roles?.includes('admin') ||
        user.roles?.includes('ROLE_ADMIN')),
  )

  return (
    <header className="fixed top-0 left-0 w-full z-50 glass-header h-16 shadow-sm">
      <div className="flex justify-between items-center w-full px-4 md:px-8 max-w-7xl mx-auto h-full">
        {/* Brand Logo */}
        <div
          onClick={() => setActiveTab('search')}
          className="flex items-center gap-3 cursor-pointer group"
        >
          <div className="w-10 h-10 rounded-xl bg-gradient-to-tr from-slate-900 to-sky-600 flex items-center justify-center text-white shadow-md shadow-sky-500/20 group-hover:scale-105 transition-transform">
            <span className="material-symbols-outlined text-2xl" data-icon="flight_takeoff">
              flight_takeoff
            </span>
          </div>
          <div className="flex flex-col">
            <span className="text-xl font-extrabold tracking-tight text-slate-900 flex items-center gap-1.5">
              SkySwift <span className="text-sky-600 font-semibold text-sm">Airlines</span>
            </span>
            <span className="text-[10px] tracking-wider text-slate-400 font-bold uppercase -mt-1">
              Horizon Ethos
            </span>
          </div>
        </div>

        {/* Navigation Links */}
        <nav className="hidden md:flex items-center gap-1 bg-slate-100/80 p-1 rounded-full border border-slate-200/60">
          <button
            onClick={() => setActiveTab('search')}
            className={`px-4 py-1.5 rounded-full text-sm font-semibold transition-all duration-200 flex items-center gap-1.5 ${
              activeTab === 'search'
                ? 'bg-slate-900 text-white shadow-sm'
                : 'text-slate-600 hover:text-slate-900 hover:bg-slate-200/50'
            }`}
          >
            <span className="material-symbols-outlined text-lg">search</span>
            Tìm chuyến bay
          </button>

          <button
            onClick={() => setActiveTab('bookings')}
            className={`px-4 py-1.5 rounded-full text-sm font-semibold transition-all duration-200 flex items-center gap-1.5 ${
              activeTab === 'bookings'
                ? 'bg-slate-900 text-white shadow-sm'
                : 'text-slate-600 hover:text-slate-900 hover:bg-slate-200/50'
            }`}
          >
            <span className="material-symbols-outlined text-lg">confirmation_number</span>
            Vé của tôi
          </button>

          <button
            onClick={onOpenProfileModal}
            className={`px-4 py-1.5 rounded-full text-sm font-semibold transition-all duration-200 flex items-center gap-1.5 ${
              activeTab === 'profile'
                ? 'bg-slate-900 text-white shadow-sm'
                : 'text-slate-600 hover:text-slate-900 hover:bg-slate-200/50'
            }`}
          >
            <span className="material-symbols-outlined text-lg">card_membership</span>
            Hồ sơ & Dặm
          </button>

          {/* Admin button ONLY shown when user is logged in AND possesses ADMIN role */}
          {isAdmin && (
            <button
              onClick={() => setActiveTab('admin')}
              className={`px-4 py-1.5 rounded-full text-sm font-semibold transition-all duration-200 flex items-center gap-1.5 ${
                activeTab === 'admin'
                  ? 'bg-sky-600 text-white shadow-sm'
                  : 'text-sky-700 hover:bg-sky-100/60 font-bold'
              }`}
            >
              <span className="material-symbols-outlined text-lg">admin_panel_settings</span>
              Quản trị
            </button>
          )}
        </nav>

        {/* User Actions */}
        <div className="flex items-center gap-3">
          {/* Notifications Drawer Toggle */}
          <button
            onClick={onToggleNotifications}
            className="relative p-2.5 text-slate-600 hover:text-slate-900 hover:bg-slate-100 rounded-full transition-colors"
            title="Thông báo"
          >
            <span className="material-symbols-outlined text-xl">notifications</span>
            {unreadNotificationsCount > 0 && (
              <span className="absolute top-1 right-1 w-4 h-4 bg-orange-500 text-white text-[10px] font-bold rounded-full flex items-center justify-center border-2 border-white animate-pulse">
                {unreadNotificationsCount}
              </span>
            )}
          </button>

          {/* User Auth Pill / Buttons */}
          {user ? (
            <div className="flex items-center gap-2">
              <button
                onClick={onOpenProfileModal}
                className="flex items-center gap-2 p-1.5 pr-3 bg-slate-100 hover:bg-slate-200/80 rounded-full border border-slate-200 transition-colors"
              >
                <div className="w-7 h-7 rounded-full bg-slate-900 text-white font-bold text-xs flex items-center justify-center uppercase">
                  {user.fullName?.[0] || 'U'}
                </div>
                <div className="flex flex-col text-left">
                  <span className="text-xs font-bold text-slate-800 leading-tight">
                    {user.fullName}
                  </span>
                  <span className="text-[10px] text-slate-500 font-medium leading-none">
                    {user.email}
                  </span>
                </div>
              </button>

              <button
                onClick={onLogout}
                className="p-2 text-slate-400 hover:text-red-600 hover:bg-red-50 rounded-full transition-colors"
                title="Đăng xuất"
              >
                <span className="material-symbols-outlined text-xl">logout</span>
              </button>
            </div>
          ) : (
            <div className="flex items-center gap-2">
              <button
                onClick={onLogin}
                className="px-4 py-2 text-sm font-semibold text-slate-700 hover:text-slate-900 transition-colors"
              >
                Đăng nhập
              </button>
              <button
                onClick={onRegister}
                className="px-4 py-2 text-sm font-semibold bg-sky-600 hover:bg-sky-700 text-white rounded-lg shadow-sm shadow-sky-600/30 transition-colors"
              >
                Đăng ký
              </button>
            </div>
          )}
        </div>
      </div>
    </header>
  )
}
