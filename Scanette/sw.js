var cacheName = 'sw';
var contentToCache = [
  '/Scanette/',
  '/Scanette/index.html',
  '/Scanette/style.css',
  '/Scanette/manifest.json',
  '/Scanette/sw.js',
  '/Scanette/produits.csv',
  '/Scanette/js/DecoderWorker.js',
  '/Scanette/js/exif.js',
  '/Scanette/js/job.js',
  '/Scanette/js/app.js',
  '/Scanette/favicon.ico',
  '/Scanette/images/logo.png',
  '/Scanette/images/barcode-scanner.png',
  '/Scanette/images/icon-setup.png',
  '/Scanette/images/icon-transmit.png',
  '/Scanette/icons/icon-32.png',
  '/Scanette/icons/icon-64.png',
  '/Scanette/icons/icon-96.png',
  '/Scanette/icons/icon-128.png',
  '/Scanette/icons/icon-168.png',
  '/Scanette/icons/icon-192.png',
  '/Scanette/icons/icon-256.png',
  '/Scanette/icons/icon-512.png'
];

self.addEventListener('install', (e) => {
  console.log('[Service Worker] Install');
  e.waitUntil(
    caches.open(cacheName).then((cache) => {
          console.log('[Service Worker] Caching all: app shell and content');
      return cache.addAll(contentToCache);
    })
  );
});

self.addEventListener('fetch', (e) => {
  e.respondWith(
    caches.match(e.request).then((r) => {
          console.log('[Service Worker] Fetching resource: '+e.request.url);
      return r || fetch(e.request).then((response) => {
                return caches.open(cacheName).then((cache) => {
          console.log('[Service Worker] Caching new resource: '+e.request.url);
          cache.put(e.request, response.clone());
          return response;
        });
      });
    })
  );
});

