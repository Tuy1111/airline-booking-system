import React, { useState } from 'react'
import type { UserView } from '../features/users/types'

type UserProfilePageProps = {
  profile: UserView
  onBack: () => void
  onUpdateProfile: (data: {
    fullName: string
    phone: string
    dateOfBirth?: string
    gender?: 'MALE' | 'FEMALE' | 'OTHER' | ''
    nationality?: string
  }) => void
  onSubmitPassport: (data: {
    passportNumber: string
    issuingCountry: string
    expiryDate: string
  }) => void
  onEarnMiles: (miles: number, reason: string) => void
  onRedeemMiles: (miles: number, reason: string) => void
  isUpdating: boolean
}

export const UserProfilePage: React.FC<UserProfilePageProps> = ({
  profile,
  onBack,
  onUpdateProfile,
  onSubmitPassport,
  onEarnMiles,
  onRedeemMiles,
  isUpdating,
}) => {
  const [tab, setTab] = useState<'profile' | 'passport' | 'loyalty'>('profile')

  const [fullName, setFullName] = useState(profile?.fullName || '')
  const [phone, setPhone] = useState(profile?.phone || '')
  const [dateOfBirth, setDateOfBirth] = useState(profile?.dateOfBirth || '')
  const [gender, setGender] = useState<'MALE' | 'FEMALE' | 'OTHER' | ''>(
    (profile?.gender as 'MALE' | 'FEMALE' | 'OTHER') || '',
  )
  const [nationality] = useState(profile?.nationality || 'VNM')

  const [passportNumber, setPassportNumber] = useState(profile?.passportNumber || '')
  const [issuingCountry, setIssuingCountry] = useState(profile?.passportCountry || 'VNM')
  const [expiryDate, setExpiryDate] = useState(profile?.passportExpiry || '')

  const [milesAmount, setMilesAmount] = useState(1000)
  const [milesReason, setMilesReason] = useState('Chuyến bay hoàn thành VN123')

  return (
    <section className="mx-auto w-full max-w-6xl px-4 py-10 sm:px-6 lg:py-14">
      <button
        type="button"
        onClick={onBack}
        className="inline-flex items-center gap-2 text-sm font-semibold text-slate-500 transition-colors hover:text-slate-900"
      >
        <span className="material-symbols-outlined text-lg" aria-hidden="true">arrow_back</span>
        Về trang chủ
      </button>

      <header className="mt-7 flex flex-col justify-between gap-6 border-b border-slate-200 pb-8 md:flex-row md:items-end">
        <div>
          <p className="text-xs font-bold uppercase tracking-[0.18em] text-sky-700">Tài khoản SkySwift</p>
          <h1 className="mt-2 text-4xl font-black tracking-[-0.04em] text-slate-950 sm:text-5xl">Hồ sơ hành khách</h1>
          <p className="mt-3 max-w-xl text-sm leading-6 text-slate-500">
            Quản lý thông tin cá nhân, hộ chiếu và quyền lợi thành viên của bạn.
          </p>
        </div>
        <div className="flex items-center gap-4">
          <div className="flex h-14 w-14 items-center justify-center rounded-2xl bg-slate-900 text-xl font-black text-white">
            {profile.fullName?.[0] || 'U'}
          </div>
          <div className="flex items-center gap-4">
            <div>
              <h2 className="text-lg font-extrabold text-slate-950">{profile.fullName}</h2>
              <p className="text-xs text-slate-500">{profile.email}</p>
              <div className="flex items-center gap-2 mt-1">
                <span className="text-[10px] font-bold uppercase tracking-wider text-slate-500">
                  Hạng {profile.loyaltyTier || 'BRONZE'}
                </span>
                <span className={`text-[10px] font-bold ${
                  profile.kycStatus === 'VERIFIED'
                    ? 'text-emerald-600'
                    : profile.kycStatus === 'REJECTED'
                      ? 'text-rose-600'
                      : 'text-amber-600'
                }`}>
                  KYC: {profile.kycStatus || 'UNVERIFIED'}
                </span>
              </div>
            </div>
          </div>
        </div>
      </header>

      <div className="mt-8 grid items-start gap-8 lg:grid-cols-[14rem_minmax(0,1fr)]">
        <nav className="flex gap-2 overflow-x-auto lg:flex-col" aria-label="Các mục hồ sơ">
          <button
            type="button"
            onClick={() => setTab('profile')}
            className={`whitespace-nowrap rounded-xl px-4 py-3 text-left text-sm font-semibold transition-all active:scale-[0.98] ${
              tab === 'profile'
                ? 'bg-slate-900 text-white'
                : 'text-slate-600 hover:bg-slate-100 hover:text-slate-950'
            }`}
          >
            Thông tin cá nhân
          </button>
          <button
            type="button"
            onClick={() => setTab('passport')}
            className={`whitespace-nowrap rounded-xl px-4 py-3 text-left text-sm font-semibold transition-all active:scale-[0.98] ${
              tab === 'passport'
                ? 'bg-slate-900 text-white'
                : 'text-slate-600 hover:bg-slate-100 hover:text-slate-950'
            }`}
          >
            Hộ chiếu & KYC
          </button>
          <button
            type="button"
            onClick={() => setTab('loyalty')}
            className={`whitespace-nowrap rounded-xl px-4 py-3 text-left text-sm font-semibold transition-all active:scale-[0.98] ${
              tab === 'loyalty'
                ? 'bg-slate-900 text-white'
                : 'text-slate-600 hover:bg-slate-100 hover:text-slate-950'
            }`}
          >
            Dặm thưởng
          </button>
        </nav>

        <section className="min-w-0 rounded-[2rem] bg-white p-6 shadow-[0_24px_70px_rgba(15,23,42,0.08)] sm:p-8">
          {tab === 'profile' && (
            <form
              onSubmit={(e) => {
                e.preventDefault()
                onUpdateProfile({ fullName, phone, dateOfBirth, gender, nationality })
              }}
              className="space-y-4"
            >
              <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                <div>
                  <label className="block text-xs font-bold text-slate-700 uppercase mb-1">
                    Họ và tên
                  </label>
                  <input
                    type="text"
                    required
                    value={fullName}
                    onChange={(e) => setFullName(e.target.value)}
                    className="w-full bg-slate-50 border border-slate-200 rounded-xl p-3 text-sm font-bold focus:outline-none focus:border-sky-500"
                  />
                </div>

                <div>
                  <label className="block text-xs font-bold text-slate-700 uppercase mb-1">
                    Số điện thoại
                  </label>
                  <input
                    type="text"
                    required
                    value={phone}
                    onChange={(e) => setPhone(e.target.value)}
                    className="w-full bg-slate-50 border border-slate-200 rounded-xl p-3 text-sm font-bold focus:outline-none focus:border-sky-500"
                  />
                </div>

                <div>
                  <label className="block text-xs font-bold text-slate-700 uppercase mb-1">
                    Ngày sinh
                  </label>
                  <input
                    type="date"
                    value={dateOfBirth}
                    onChange={(e) => setDateOfBirth(e.target.value)}
                    className="w-full bg-slate-50 border border-slate-200 rounded-xl p-3 text-sm font-bold focus:outline-none focus:border-sky-500"
                  />
                </div>

                <div>
                  <label className="block text-xs font-bold text-slate-700 uppercase mb-1">
                    Giới tính
                  </label>
                  <select
                    value={gender}
                    onChange={(e) =>
                      setGender(e.target.value as 'MALE' | 'FEMALE' | 'OTHER' | '')
                    }
                    className="w-full bg-slate-50 border border-slate-200 rounded-xl p-3 text-sm font-bold focus:outline-none focus:border-sky-500"
                  >
                    <option value="">Chưa chọn</option>
                    <option value="MALE">Nam (Male)</option>
                    <option value="FEMALE">Nữ (Female)</option>
                    <option value="OTHER">Khác (Other)</option>
                  </select>
                </div>
              </div>

              <div className="pt-4 flex justify-end">
                <button
                  type="submit"
                  disabled={isUpdating}
                  className="px-6 py-2.5 bg-slate-900 hover:bg-sky-600 text-white font-bold rounded-xl text-xs transition-colors"
                >
                  {isUpdating ? 'Đang cập nhật...' : 'Lưu thay đổi'}
                </button>
              </div>
            </form>
          )}

          {tab === 'passport' && (
            <form
              onSubmit={(e) => {
                e.preventDefault()
                onSubmitPassport({ passportNumber, issuingCountry, expiryDate })
              }}
              className="space-y-4"
            >
              <div className="bg-sky-50 border border-sky-200 rounded-2xl p-4 text-xs text-sky-900">
                <strong className="block font-extrabold mb-1">Xác minh hộ chiếu (KYC)</strong>
                Hệ thống yêu cầu thông tin hộ chiếu hợp lệ cho các chuyến bay quốc tế.
              </div>

              <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                <div>
                  <label className="block text-xs font-bold text-slate-700 uppercase mb-1">
                    Số hộ chiếu
                  </label>
                  <input
                    type="text"
                    required
                    value={passportNumber}
                    onChange={(e) => setPassportNumber(e.target.value)}
                    placeholder="P1234567"
                    className="w-full bg-slate-50 border border-slate-200 rounded-xl p-3 text-sm font-bold focus:outline-none focus:border-sky-500"
                  />
                </div>

                <div>
                  <label className="block text-xs font-bold text-slate-700 uppercase mb-1">
                    Quốc gia cấp
                  </label>
                  <input
                    type="text"
                    required
                    value={issuingCountry}
                    onChange={(e) => setIssuingCountry(e.target.value)}
                    placeholder="VNM"
                    className="w-full bg-slate-50 border border-slate-200 rounded-xl p-3 text-sm font-bold focus:outline-none focus:border-sky-500"
                  />
                </div>

                <div className="md:col-span-2">
                  <label className="block text-xs font-bold text-slate-700 uppercase mb-1">
                    Ngày hết hạn hộ chiếu
                  </label>
                  <input
                    type="date"
                    required
                    value={expiryDate}
                    onChange={(e) => setExpiryDate(e.target.value)}
                    className="w-full bg-slate-50 border border-slate-200 rounded-xl p-3 text-sm font-bold focus:outline-none focus:border-sky-500"
                  />
                </div>
              </div>

              <div className="pt-4 flex justify-end">
                <button
                  type="submit"
                  disabled={isUpdating}
                  className="px-6 py-2.5 bg-sky-600 hover:bg-sky-700 text-white font-bold rounded-xl text-xs transition-colors"
                >
                  {isUpdating ? 'Đang gửi...' : 'Gửi xác minh hộ chiếu'}
                </button>
              </div>
            </form>
          )}

          {tab === 'loyalty' && (
            <div className="space-y-6">
              {/* Miles Summary Cards */}
              <div className="grid grid-cols-2 gap-4">
                <div className="bg-slate-900 text-white p-5 rounded-2xl">
                  <span className="text-xs text-slate-400 font-bold block">Số dặm khả dụng</span>
                  <span className="text-3xl font-black text-amber-400">
                    {profile?.milesBalance ?? 0}
                  </span>
                  <span className="text-[10px] text-slate-400 block mt-1">Dặm thưởng SkyMiles</span>
                </div>

                <div className="bg-slate-100 text-slate-900 p-5 rounded-2xl border border-slate-200">
                  <span className="text-xs text-slate-500 font-bold block">Dặm tích lũy trọn đời</span>
                  <span className="text-3xl font-black text-slate-900">
                    {profile?.lifetimeMiles ?? 0}
                  </span>
                  <span className="text-[10px] text-slate-500 block mt-1">Dặm tích lũy</span>
                </div>
              </div>

              {/* Earn / Redeem Form */}
              <div className="bg-slate-50 p-5 rounded-2xl border border-slate-200 space-y-4">
                <h4 className="font-bold text-slate-800 text-xs uppercase tracking-wider">
                  Cộng / Đổi dặm thưởng
                </h4>

                <div className="grid grid-cols-1 md:grid-cols-2 gap-3">
                  <div>
                    <label className="block text-[11px] font-bold text-slate-600 mb-1">
                      Số dặm
                    </label>
                    <input
                      type="number"
                      value={milesAmount}
                      onChange={(e) => setMilesAmount(Number(e.target.value))}
                      className="w-full bg-white border border-slate-200 rounded-xl p-2.5 text-xs font-bold"
                    />
                  </div>

                  <div>
                    <label className="block text-[11px] font-bold text-slate-600 mb-1">Lý do</label>
                    <input
                      type="text"
                      value={milesReason}
                      onChange={(e) => setMilesReason(e.target.value)}
                      className="w-full bg-white border border-slate-200 rounded-xl p-2.5 text-xs font-bold"
                    />
                  </div>
                </div>

                <div className="flex gap-3 pt-2">
                  <button
                    onClick={() => onEarnMiles(milesAmount, milesReason)}
                    className="flex-1 py-2.5 bg-emerald-600 hover:bg-emerald-700 text-white font-bold rounded-xl text-xs"
                  >
                    + Cộng dặm
                  </button>
                  <button
                    onClick={() => onRedeemMiles(milesAmount, milesReason)}
                    className="flex-1 py-2.5 bg-orange-600 hover:bg-orange-700 text-white font-bold rounded-xl text-xs"
                  >
                    - Đổi dặm
                  </button>
                </div>
              </div>
            </div>
          )}
        </section>
      </div>
    </section>
  )
}
