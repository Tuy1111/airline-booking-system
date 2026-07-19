import React, { useState } from 'react'
import type { UserView } from '../features/users/types'

interface UserProfileModalProps {
  isOpen: boolean
  onClose: () => void
  profile: UserView | null
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

export const UserProfileModal: React.FC<UserProfileModalProps> = ({
  isOpen,
  onClose,
  profile,
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

  if (!isOpen) return null

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center p-4 bg-slate-900/60 backdrop-blur-sm animate-fade-in">
      <div className="bg-white rounded-3xl max-w-2xl w-full overflow-hidden shadow-2xl border border-slate-200 relative max-h-[90vh] flex flex-col">
        {/* Modal Header */}
        <div className="bg-slate-900 text-white p-6 relative">
          <button
            onClick={onClose}
            className="absolute top-4 right-4 text-slate-400 hover:text-white p-1 rounded-full"
          >
            <span className="material-symbols-outlined text-2xl">close</span>
          </button>

          <div className="flex items-center gap-4">
            <div className="w-14 h-14 rounded-2xl bg-gradient-to-tr from-sky-500 to-indigo-600 text-white text-xl font-black flex items-center justify-center shadow-md">
              {profile?.fullName?.[0] || 'U'}
            </div>
            <div>
              <h3 className="text-xl font-black text-white">{profile?.fullName || 'Hồ sơ người dùng'}</h3>
              <p className="text-xs text-slate-400">{profile?.email}</p>
              <div className="flex items-center gap-2 mt-1">
                <span className="px-2.5 py-0.5 bg-amber-500/20 text-amber-300 rounded-full text-[10px] font-bold uppercase tracking-wider border border-amber-400/30">
                  Hạng: {profile?.loyaltyTier || 'BRONZE'}
                </span>
                <span className="text-[10px] text-emerald-400 font-bold">
                  KYC: {profile?.kycStatus || 'VERIFIED'}
                </span>
              </div>
            </div>
          </div>
        </div>

        {/* Modal Sub-Tabs */}
        <div className="flex border-b border-slate-200 bg-slate-50 px-6 gap-4 text-xs font-bold text-slate-600">
          <button
            onClick={() => setTab('profile')}
            className={`py-3 border-b-2 transition-all ${
              tab === 'profile'
                ? 'border-sky-600 text-sky-600 font-extrabold'
                : 'border-transparent hover:text-slate-900'
            }`}
          >
            Thông tin cá nhân
          </button>
          <button
            onClick={() => setTab('passport')}
            className={`py-3 border-b-2 transition-all ${
              tab === 'passport'
                ? 'border-sky-600 text-sky-600 font-extrabold'
                : 'border-transparent hover:text-slate-900'
            }`}
          >
            Hộ chiếu & KYC
          </button>
          <button
            onClick={() => setTab('loyalty')}
            className={`py-3 border-b-2 transition-all ${
              tab === 'loyalty'
                ? 'border-sky-600 text-sky-600 font-extrabold'
                : 'border-transparent hover:text-slate-900'
            }`}
          >
            Dặm thưởng Loyalty
          </button>
        </div>

        {/* Modal Body */}
        <div className="p-6 overflow-y-auto space-y-6 flex-1 text-sm">
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
        </div>
      </div>
    </div>
  )
}
