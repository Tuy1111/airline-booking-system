import React from 'react'

interface PopularDestinationsProps {
  onSelectRoute: (from: string, to: string) => void
  formatMoney: (val: number | string | null | undefined) => string
}

const DESTINATIONS = [
  {
    from: 'HAN',
    to: 'SGN',
    fromCity: 'Hà Nội',
    toCity: 'TP. Hồ Chí Minh',
    image: 'https://images.unsplash.com/photo-1583417319070-4a69db38a482?auto=format&fit=crop&w=800&q=80',
    price: 1290000,
    tag: 'Tuyến Hot Nhất',
    flightCount: '24 chuyến/ngày',
  },
  {
    from: 'HAN',
    to: 'PQC',
    fromCity: 'Hà Nội',
    toCity: 'Phú Quốc',
    image: 'https://images.unsplash.com/photo-1540555700478-4be289fbecef?auto=format&fit=crop&w=800&q=80',
    price: 1450000,
    tag: 'Đảo Ngọc Thiên Đường',
    flightCount: '12 chuyến/ngày',
  },
  {
    from: 'SGN',
    to: 'DAD',
    fromCity: 'TP. Hồ Chí Minh',
    toCity: 'Đà Nẵng',
    image: 'https://images.unsplash.com/photo-1559592413-7cec4d0cae2b?auto=format&fit=crop&w=800&q=80',
    price: 890000,
    tag: 'Khuyến Mãi Hè',
    flightCount: '18 chuyến/ngày',
  },
  {
    from: 'HAN',
    to: 'CXR',
    fromCity: 'Hà Nội',
    toCity: 'Nha Trang',
    image: 'https://images.unsplash.com/photo-1507525428034-b723cf961d3e?auto=format&fit=crop&w=800&q=80',
    price: 990000,
    tag: 'Biển Xanh Cát Trắng',
    flightCount: '10 chuyến/ngày',
  },
]

const FEATURES = [
  {
    icon: 'verified_user',
    title: 'An Toàn & Đúng Giờ',
    desc: 'Chỉ số đúng giờ OTP đạt 96.8%, đảm bảo hành trình suôn sẻ tuyệt đối.',
  },
  {
    icon: 'airline_seat_recline_extra',
    title: 'Khoang Ghế Rộng Rãi',
    desc: 'Ghế bọc da êm ái với khoảng để chân rộng tới 34-inch đẳng cấp quốc tế.',
  },
  {
    icon: 'flatware',
    title: 'Ẩm Thực Tươi Ngon',
    desc: 'Thưởng thức các món ăn nóng và thức uống phong phú được chế biến từ bếp 5 sao.',
  },
  {
    icon: 'wifi_tethering',
    title: 'Kết Nối WiFi Trên Không',
    desc: 'Lướt web, làm việc và nhắn tin trực tuyến ngay trên chuyến bay ở độ cao 10.000m.',
  },
]

export const PopularDestinations: React.FC<PopularDestinationsProps> = ({
  onSelectRoute,
  formatMoney,
}) => {
  return (
    <div className="max-w-6xl mx-auto px-4 py-12 space-y-16">
      {/* Popular Destinations Shelf */}
      <section className="space-y-6">
        <div className="flex flex-wrap items-end justify-between gap-4">
          <div>
            <span className="text-xs font-bold text-sky-600 uppercase tracking-widest">
              Ưu đãi hấp dẫn
            </span>
            <h2 className="text-2xl md:text-3xl font-black text-slate-900 tracking-tight mt-1">
              Điểm đến phổ biến & Giá vé Hot
            </h2>
          </div>
          <p className="text-xs text-slate-500 font-medium max-w-xs">
            Khám phá những điểm đến du lịch nổi tiếng nhất Việt Nam với mức giá ưu đãi đặc biệt hôm nay.
          </p>
        </div>

        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-6">
          {DESTINATIONS.map((item, idx) => (
            <div
              key={idx}
              className="group bg-white rounded-3xl overflow-hidden border border-slate-200 shadow-sm hover:shadow-xl transition-all duration-300 flex flex-col"
            >
              <div className="relative h-48 overflow-hidden">
                <img
                  src={item.image}
                  alt={item.toCity}
                  className="w-full h-full object-cover group-hover:scale-110 transition-transform duration-500"
                />
                <div className="absolute inset-0 bg-gradient-to-t from-slate-900/80 via-transparent to-transparent" />
                <span className="absolute top-3 left-3 px-3 py-1 bg-sky-600/90 backdrop-blur-md text-white text-[10px] font-bold rounded-full border border-sky-400/30">
                  {item.tag}
                </span>
                <div className="absolute bottom-3 left-3 text-white">
                  <span className="text-xs text-slate-300 font-bold block">{item.fromCity} ➔</span>
                  <h3 className="text-lg font-black tracking-tight">{item.toCity}</h3>
                </div>
              </div>

              <div className="p-4 flex-1 flex flex-col justify-between space-y-4">
                <div className="flex items-center justify-between text-xs text-slate-500 font-semibold">
                  <span className="flex items-center gap-1">
                    <span className="material-symbols-outlined text-sm text-sky-600">schedule</span>
                    {item.flightCount}
                  </span>
                  <span className="text-slate-400">Một chiều</span>
                </div>

                <div className="flex items-center justify-between pt-2 border-t border-slate-100">
                  <div>
                    <span className="text-[10px] text-slate-400 font-bold uppercase block">Chỉ từ</span>
                    <span className="text-lg font-extrabold text-orange-600">
                      {formatMoney(item.price)}
                    </span>
                  </div>
                  <button
                    onClick={() => onSelectRoute(item.from, item.to)}
                    className="px-4 py-2 bg-slate-900 hover:bg-sky-600 text-white rounded-xl text-xs font-bold transition-all shadow-md group-hover:scale-105"
                  >
                    Săn vé ngay
                  </button>
                </div>
              </div>
            </div>
          ))}
        </div>
      </section>

      {/* Service Highlights Shelf */}
      <section className="bg-gradient-to-br from-slate-900 to-sky-950 text-white p-8 md:p-12 rounded-3xl shadow-2xl space-y-8">
        <div className="text-center max-w-2xl mx-auto space-y-2">
          <span className="text-xs font-bold text-sky-400 uppercase tracking-widest">
            Trải nghiệm đẳng cấp
          </span>
          <h2 className="text-2xl md:text-4xl font-black tracking-tight">
            Tại sao nên chọn SkySwift Airlines?
          </h2>
          <p className="text-slate-300 text-xs md:text-sm">
            Chúng tôi cam kết mang lại hành trình bay tiện nghi, an toàn và ngập tràn niềm vui cho từng hành khách.
          </p>
        </div>

        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-6">
          {FEATURES.map((feat, idx) => (
            <div
              key={idx}
              className="bg-white/10 backdrop-blur-md border border-white/10 p-6 rounded-2xl space-y-3 hover:bg-white/15 transition-all"
            >
              <div className="w-12 h-12 rounded-xl bg-sky-500/20 border border-sky-400/30 text-sky-300 flex items-center justify-center font-bold">
                <span className="material-symbols-outlined text-2xl">{feat.icon}</span>
              </div>
              <h3 className="text-base font-extrabold text-white">{feat.title}</h3>
              <p className="text-xs text-slate-300 font-normal leading-relaxed">{feat.desc}</p>
            </div>
          ))}
        </div>
      </section>
    </div>
  )
}
