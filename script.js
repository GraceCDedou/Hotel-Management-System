// ============ NAVBAR ============
const navbar = document.getElementById('navbar');
const hero   = document.getElementById('hero');

function updateNavbar() {
  const heroBottom = hero ? hero.getBoundingClientRect().bottom : 0;
  if (heroBottom > 80) {
    navbar.classList.add('at-hero');
    navbar.classList.remove('scrolled');
  } else {
    navbar.classList.remove('at-hero');
    navbar.classList.add('scrolled');
  }
}

updateNavbar();
window.addEventListener('scroll', updateNavbar, { passive: true });

// ============ HAMBURGER ============
const hamburger = document.getElementById('hamburger');
const navLinks  = document.getElementById('navLinks');

if (hamburger && navLinks) {
  hamburger.addEventListener('click', () => {
    navLinks.classList.toggle('open');
  });

  document.querySelectorAll('.nav-links a, .nav-cta').forEach(link => {
    link.addEventListener('click', () => navLinks.classList.remove('open'));
  });
}

// ============ HERO SLIDESHOW ============
const slides = document.querySelectorAll('.hero-slide');
const dots   = document.querySelectorAll('.dot');
let current  = 0;
let timer;

function goToSlide(n) {
  if (!slides.length) return;
  slides[current].classList.remove('active');
  if (dots[current]) dots[current].classList.remove('active');
  current = (n + slides.length) % slides.length;
  slides[current].classList.add('active');
  if (dots[current]) dots[current].classList.add('active');
}

function nextSlide() {
  goToSlide(current + 1);
}

function startTimer() {
  if (slides.length) timer = setInterval(nextSlide, 5000);
}

startTimer();

dots.forEach(dot => {
  dot.addEventListener('click', () => {
    clearInterval(timer);
    goToSlide(parseInt(dot.dataset.slide));
    startTimer();
  });
});

// ============ GALLERY LIGHTBOX ============
const galleryItems = document.querySelectorAll('.gallery-item');
const lightbox     = document.getElementById('lightbox');
const lbImg        = document.getElementById('lbImg');
const lbClose      = document.getElementById('lbClose');
const lbPrev       = document.getElementById('lbPrev');
const lbNext       = document.getElementById('lbNext');

const galleryImages = Array.from(galleryItems).map(item => item.querySelector('img').src);
let lbIndex = 0;

function openLightbox(index) {
  lbIndex = index;
  lbImg.src = galleryImages[lbIndex];
  lightbox.classList.add('open');
  lightbox.setAttribute('aria-hidden', 'false');
  document.body.style.overflow = 'hidden';
}

function closeLightbox() {
  lightbox.classList.remove('open');
  lightbox.setAttribute('aria-hidden', 'true');
  document.body.style.overflow = '';
}

galleryItems.forEach((item, i) => {
  item.addEventListener('click', () => openLightbox(i));
});

if (lbClose) lbClose.addEventListener('click', closeLightbox);

if (lbPrev) {
  lbPrev.addEventListener('click', () => {
    lbIndex = (lbIndex - 1 + galleryImages.length) % galleryImages.length;
    lbImg.src = galleryImages[lbIndex];
  });
}

if (lbNext) {
  lbNext.addEventListener('click', () => {
    lbIndex = (lbIndex + 1) % galleryImages.length;
    lbImg.src = galleryImages[lbIndex];
  });
}

if (lightbox) {
  lightbox.addEventListener('click', e => {
    if (e.target === lightbox) closeLightbox();
  });
}

document.addEventListener('keydown', e => {
  if (!lightbox || !lightbox.classList.contains('open')) return;
  if (e.key === 'Escape') closeLightbox();
  if (e.key === 'ArrowLeft' && lbPrev) lbPrev.click();
  if (e.key === 'ArrowRight' && lbNext) lbNext.click();
});

// ============ SCROLL REVEAL ============
const revealEls = document.querySelectorAll(
  '.room-card, .offer-card, .amenity-card, .kids-card, .gallery-item, .about-image-wrap, .about-text, .stat, .contact-info, .contact-form-wrap'
);

revealEls.forEach(el => el.classList.add('reveal'));

const revealObserver = new IntersectionObserver((entries) => {
  entries.forEach((entry, i) => {
    if (entry.isIntersecting) {
      setTimeout(() => entry.target.classList.add('visible'), i * 60);
      revealObserver.unobserve(entry.target);
    }
  });
}, { threshold: 0.1 });

revealEls.forEach(el => revealObserver.observe(el));

// ============ OPTIONAL SPECIAL OFFER CLAIM ============
const offerSelect = document.getElementById('offerSelect');

document.querySelectorAll('.claim-offer-btn').forEach(btn => {
  btn.addEventListener('click', () => {
    if (offerSelect && btn.dataset.offer) {
      offerSelect.value = btn.dataset.offer;
    }
  });
});

// ============ HELPER: READS FORM INPUTS AUTOMATICALLY ============
function getFieldValue(form, names, fallbackIndex) {
  for (const n of names) {
    const el = form.querySelector(`[name="${n}"]`);
    if (el && el.value.trim() !== '') return el.value.trim();
  }
  const fields = form.querySelectorAll('input:not([type="hidden"]), select, textarea');
  return fields[fallbackIndex] ? fields[fallbackIndex].value.trim() : '';
}

// ============ 1. BOOKING FORM -> SENDS TO XAMPP ============
const bookingForm = document.getElementById('bookingForm');
const formSuccess = document.getElementById('formSuccess');

if (bookingForm) {
  bookingForm.addEventListener('submit', async e => {
    e.preventDefault();
    if (!bookingForm.checkValidity()) {
      bookingForm.reportValidity();
      return;
    }

    const formData = new FormData();
    formData.append('action',    'book_room');
    formData.append('name',      getFieldValue(bookingForm, ['name', 'guest_name'], 0));
    formData.append('email',     getFieldValue(bookingForm, ['email', 'guest_email'], 1));
    formData.append('check_in',  getFieldValue(bookingForm, ['check_in', 'checkin'], 2));
    formData.append('check_out', getFieldValue(bookingForm, ['check_out', 'checkout'], 3));
    formData.append('room',      getFieldValue(bookingForm, ['room', 'room_type'], 4));
    formData.append('guests',    getFieldValue(bookingForm, ['guests', 'num_guests'], 5) || '1');
    formData.append('requests',  getFieldValue(bookingForm, ['requests', 'special_requests'], 6));

    try {
      const response = await fetch('api.php', { method: 'POST', body: formData });
      const result   = await response.json();

      if (result.status === 'success') {
        bookingForm.classList.add('hidden');
        if (formSuccess) {
          formSuccess.classList.remove('hidden');
          const msgP = formSuccess.querySelector('p');
          if (msgP && result.reference) {
            msgP.textContent = `Reference #${result.reference} saved! Our team has received your booking.`;
          }
        }
      } else {
        alert('Booking error: ' + (result.message || 'Could not save booking.'));
      }
    } catch (err) {
      alert('Cannot reach api.php. Make sure Apache is running in XAMPP and the URL starts with http://localhost/');
    }
  });
}

// ============ 2. CONTACT FORM -> SENDS TO NOTIFICATIONS ============
const contactForm    = document.getElementById('contactForm');
const contactSuccess = document.getElementById('contactSuccess');

if (contactForm) {
  contactForm.addEventListener('submit', async e => {
    e.preventDefault();
    if (!contactForm.checkValidity()) {
      contactForm.reportValidity();
      return;
    }

    const formData = new FormData();
    formData.append('action',  'send_message');
    formData.append('name',    getFieldValue(contactForm, ['name'], 0));
    formData.append('email',   getFieldValue(contactForm, ['email'], 1));
    formData.append('message', getFieldValue(contactForm, ['message'], 2));

    try {
      const response = await fetch('api.php', { method: 'POST', body: formData });
      const result   = await response.json();

      if (result.status === 'success') {
        if (contactSuccess) contactSuccess.classList.remove('hidden');
        contactForm.reset();
      } else {
        alert('Message error: ' + (result.message || 'Could not send message.'));
      }
    } catch (err) {
      alert('Cannot reach api.php. Make sure Apache is running in XAMPP.');
    }
  });
}

// ============ DINING & GALLERY TABS ============
const diningTabs  = document.querySelectorAll('.dtab');
const diningCards = document.querySelectorAll('.dining-card');
diningTabs.forEach(tab => {
  tab.addEventListener('click', () => {
    diningTabs.forEach(t => t.classList.remove('active'));
    tab.classList.add('active');
    const filter = tab.dataset.tab;
    diningCards.forEach(card => {
      card.classList.toggle('hidden-card', filter !== 'all' && card.dataset.category !== filter);
    });
  });
});

const galleryTabs     = document.querySelectorAll('.gtab');
const galleryItemsAll = document.querySelectorAll('.gallery-item');
galleryTabs.forEach(tab => {
  tab.addEventListener('click', () => {
    galleryTabs.forEach(t => t.classList.remove('active'));
    tab.classList.add('active');
    const filter = tab.dataset.gtab;
    galleryItemsAll.forEach(item => {
      item.classList.toggle('hidden-item', filter !== 'all' && item.dataset.gtag !== filter);
    });
  });
});

// ============ STAR RATINGS ============
const rateStars = document.querySelectorAll('.rate-star');
const fbRating  = document.getElementById('fbRating');

rateStars.forEach(star => {
  star.addEventListener('mouseover', () => {
    const val = parseInt(star.dataset.val);
    rateStars.forEach((s, i) => s.classList.toggle('active', i < val));
  });
  star.addEventListener('mouseleave', () => {
    const current = parseInt(fbRating.value);
    rateStars.forEach((s, i) => s.classList.toggle('active', i < current));
  });
  star.addEventListener('click', () => {
    fbRating.value = star.dataset.val;
    const val = parseInt(star.dataset.val);
    rateStars.forEach((s, i) => s.classList.toggle('active', i < val));
  });
});

document.querySelectorAll('.mini-stars').forEach(group => {
  const stars = group.querySelectorAll('.ms');
  stars.forEach(star => {
    star.addEventListener('mouseover', () => {
      const val = parseInt(star.dataset.val);
stars.forEach((s, i) => s.classList.toggle('active', i < val));
});
star.addEventListener('mouseleave', () => {
const current = parseInt(group.dataset.selected || 0);
stars.forEach((s, i) => s.classList.toggle('active', i < current));
});
star.addEventListener('click', () => {
group.dataset.selected = star.dataset.val;
const val = parseInt(star.dataset.val);
stars.forEach((s, i) => s.classList.toggle('active', i < val));
});
});
});
// ============ 3. FEEDBACK FORM ("SHARE YOUR EXPERIENCE") -> SENDS TO NOTIFICATIONS ============
const feedbackForm = document.getElementById('feedbackForm');
const fbSuccess    = document.getElementById('fbSuccess');
if (feedbackForm) {
feedbackForm.addEventListener('submit', async e => {
e.preventDefault();
if (fbRating && parseInt(fbRating.value) === 0) {
fbRating.setCustomValidity('Please select a rating.');
} else if (fbRating) {
fbRating.setCustomValidity('');
}
if (!feedbackForm.checkValidity()) {
feedbackForm.reportValidity();
return;
}
const formData = new FormData();
formData.append('action', 'submit_review');
formData.append('name',   getFieldValue(feedbackForm, ['name'], 0));
formData.append('email',  getFieldValue(feedbackForm, ['email'], 1));
formData.append('rating', fbRating ? fbRating.value : '5');
formData.append('review', getFieldValue(feedbackForm, ['review'], 2));
try {
const response = await fetch('api.php', { method: 'POST', body: formData });
const result   = await response.json();
if (result.status === 'success') {
if (fbSuccess) fbSuccess.classList.remove('hidden');
feedbackForm.reset();
rateStars.forEach(s => s.classList.remove('active'));
if (fbRating) fbRating.value = 0;
document.querySelectorAll('.ms').forEach(s => s.classList.remove('active'));
document.querySelectorAll('.mini-stars').forEach(g => g.dataset.selected = 0);
setTimeout(() => { if (fbSuccess) fbSuccess.classList.add('hidden'); }, 6000);
} else {
alert('Review error: ' + (result.message || 'Could not submit review.'));
}
} catch (err) {
alert('Cannot reach api.php. Make sure Apache is running in XAMPP.');
}
});
}
