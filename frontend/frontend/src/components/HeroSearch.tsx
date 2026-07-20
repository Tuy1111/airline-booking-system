import React from 'react'
import heroBg from '../assets/skyswift-coast-hero.webp'

interface HeroSearchProps {
  onGoToSearch: () => void
}

export const HeroSearch: React.FC<HeroSearchProps> = ({ onGoToSearch }) => (
  <section className="booking-hero" aria-labelledby="hero-title">
    <div className="hero-content">
      <div className="hero-copy">
        <p className="eyebrow light">Hành trình bắt đầu tại đây</p>
        <h1 id="hero-title">Bay đúng nhịp.<br />Đến đúng nơi.</h1>
        <p className="hero-lede">
          Tìm chuyến bay nội địa, chọn ghế và thanh toán trong một hành trình rõ ràng.
        </p>
        <button type="button" className="hero-cta" onClick={onGoToSearch}>
          Tìm chuyến bay
          <span className="material-symbols-outlined" aria-hidden="true">arrow_forward</span>
        </button>
      </div>
      <figure className="hero-visual">
        <img src={heroBg} alt="Cánh máy bay SkySwift trên đường bờ biển Việt Nam lúc bình minh" />
        <figcaption>
          <span>SkySwift routebook</span>
          <strong>Hành trình rõ ràng, từ lúc tìm vé đến khi hạ cánh.</strong>
        </figcaption>
      </figure>
    </div>
  </section>
)
