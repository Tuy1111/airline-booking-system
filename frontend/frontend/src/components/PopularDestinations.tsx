import React from 'react'

interface PopularDestinationsProps {
  onSelectRoute: (from: string, to: string) => void
  formatMoney: (val: number | string | null | undefined) => string
}

const ROUTES = [
  { from: 'HAN', to: 'SGN', fromCity: 'Hà Nội', toCity: 'TP. Hồ Chí Minh', price: 1290000, frequency: '24 chuyến mỗi ngày' },
  { from: 'SGN', to: 'DAD', fromCity: 'TP. Hồ Chí Minh', toCity: 'Đà Nẵng', price: 890000, frequency: '18 chuyến mỗi ngày' },
  { from: 'HAN', to: 'PQC', fromCity: 'Hà Nội', toCity: 'Phú Quốc', price: 1450000, frequency: '12 chuyến mỗi ngày' },
  { from: 'HAN', to: 'CXR', fromCity: 'Hà Nội', toCity: 'Nha Trang', price: 990000, frequency: '10 chuyến mỗi ngày' },
]

const BOOKING_PILLARS = [
  {
    icon: 'route',
    title: 'Route clarity',
    text: 'Giờ bay, điểm đi và điểm đến luôn nằm trong cùng một nhịp đọc.',
  },
  {
    icon: 'airline_seat_recline_extra',
    title: 'Seat control',
    text: 'Chọn đúng chỗ ngồi bạn muốn trước khi bước sang phần thanh toán.',
  },
  {
    icon: 'receipt_long',
    title: 'Payment trail',
    text: 'Mã đặt chỗ và trạng thái thanh toán được giữ lại trong tài khoản.',
  },
]

export const PopularDestinations: React.FC<PopularDestinationsProps> = ({
  onSelectRoute,
  formatMoney,
}) => (
  <div className="landing-content homepage-redesign">
    <section className="route-section" aria-labelledby="route-heading">
      <div className="route-intro">
        <div className="section-heading">
          <p className="eyebrow">Đường bay được chọn nhiều</p>
          <h2 id="route-heading">Bắt đầu bằng một cặp thành phố.</h2>
          <p>Chọn một hành trình bên dưới để mở trang tìm vé với điểm đi và điểm đến đã sẵn sàng.</p>
        </div>
        <span className="route-note">Giá một chiều, đã gồm thuế phí</span>
      </div>

      <div className="route-list">
        {ROUTES.map((route, index) => (
          <article className="route-row" key={`${route.from}-${route.to}`}>
            <span className="route-index" aria-hidden="true">{String(index + 1).padStart(2, '0')}</span>
            <div className="route-main">
              <span className="route-code">{route.from} <span aria-hidden="true">→</span> {route.to}</span>
              <h3>{route.fromCity} <span aria-hidden="true">→</span> {route.toCity}</h3>
            </div>
            <span className="route-frequency">{route.frequency}</span>
            <div className="route-price">
              <small>Từ</small>
              <strong>{formatMoney(route.price)}</strong>
            </div>
            <button type="button" onClick={() => onSelectRoute(route.from, route.to)} aria-label={`Tìm vé ${route.fromCity} đến ${route.toCity}`}>
              <span className="material-symbols-outlined" aria-hidden="true">north_east</span>
            </button>
          </article>
        ))}
      </div>
    </section>

    <section className="promise-section" aria-labelledby="promise-heading">
      <div className="promise-lead">
        <p className="eyebrow">Một cách đặt vé dễ hiểu</p>
        <h2 id="promise-heading">Ít bước hơn. Nhiều chủ động hơn.</h2>
        <p>SkySwift gom những thông tin quan trọng về một nơi để bạn quyết định nhanh mà vẫn thấy chắc chắn.</p>
      </div>
      <div className="promise-list">
        {BOOKING_PILLARS.map((pillar, index) => (
          <article className="promise-row" key={pillar.title}>
            <span className="promise-number">{String(index + 1).padStart(2, '0')}</span>
            <span className="promise-icon material-symbols-outlined" aria-hidden="true">{pillar.icon}</span>
            <div>
              <h3>{pillar.title}</h3>
              <p>{pillar.text}</p>
            </div>
          </article>
        ))}
      </div>
    </section>

    <section className="homepage-cta" aria-labelledby="homepage-cta-heading">
      <div>
        <p className="eyebrow light">Sẵn sàng khởi hành</p>
        <h2 id="homepage-cta-heading">Chuyến bay tiếp theo của bạn bắt đầu từ đây.</h2>
      </div>
      <button type="button" onClick={() => onSelectRoute('HAN', 'SGN')}>
        Tìm chuyến bay
        <span className="material-symbols-outlined" aria-hidden="true">arrow_forward</span>
      </button>
    </section>

    <footer className="site-footer">
      <div>
        <strong>SkySwift Airlines</strong>
        <p>Hệ thống đặt vé và vận hành chuyến bay.</p>
      </div>
      <nav aria-label="Liên kết cuối trang">
        <a href="mailto:support@skyswift.vn">Hỗ trợ</a>
        <a href="/privacy">Quyền riêng tư</a>
        <a href="/terms">Điều khoản</a>
      </nav>
      <span>© {new Date().getFullYear()} SkySwift</span>
    </footer>
  </div>
)
