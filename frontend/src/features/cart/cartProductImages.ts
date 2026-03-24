function toDataUri(svg: string) {
  return `data:image/svg+xml;utf8,${encodeURIComponent(svg)}`
}

function coffeeCardSvg({
  title,
  bg1,
  bg2,
}: {
  title: string
  bg1: string
  bg2: string
}) {
  const safeTitle = title.length > 10 ? `${title.slice(0, 10)}…` : title

  return `
<svg xmlns="http://www.w3.org/2000/svg" width="160" height="160" viewBox="0 0 160 160">
  <defs>
    <linearGradient id="g" x1="0" y1="0" x2="1" y2="1">
      <stop offset="0" stop-color="${bg1}"/>
      <stop offset="1" stop-color="${bg2}"/>
    </linearGradient>
    <filter id="s" x="-20%" y="-20%" width="140%" height="140%">
      <feDropShadow dx="0" dy="6" stdDeviation="6" flood-color="rgba(0,0,0,0.18)"/>
    </filter>
  </defs>

  <rect x="14" y="14" width="132" height="132" rx="26" fill="url(#g)"/>
  <g filter="url(#s)">
    <circle cx="58" cy="88" r="30" fill="rgba(255,255,255,0.26)"/>
    <ellipse cx="102" cy="78" rx="36" ry="44" fill="rgba(0,0,0,0.12)"/>
    <path d="M79 56c-14 7-23 21-23 36 0 19 14 34 23 34s23-15 23-34c0-15-9-29-23-36z" fill="rgba(255,255,255,0.18)"/>
    <g transform="translate(0,2)" opacity="0.95">
      <path d="M68 74c9-10 18-16 33-17-7 12-6 21 2 30-12 5-22 10-35-13z" fill="rgba(0,0,0,0.20)"/>
      <path d="M72 98c10-8 20-11 31-7-7 12-15 19-31 7z" fill="rgba(0,0,0,0.18)"/>
    </g>
  </g>

  <text x="80" y="138" text-anchor="middle"
        font-family="system-ui, -apple-system, Segoe UI, Roboto, sans-serif"
        font-size="14" font-weight="800" fill="rgba(255,255,255,0.92)">${safeTitle}</text>
</svg>
`.trim()
}

export const productImageSrcByName: Record<string, string> = {
  Columbia: toDataUri(
    coffeeCardSvg({
      title: 'Columbia',
      bg1: '#5B8FFF',
      bg2: '#AA3BFF',
    }),
  ),
  Ethiopia: toDataUri(
    coffeeCardSvg({
      title: 'Ethiopia',
      bg1: '#2DD4BF',
      bg2: '#5B8FFF',
    }),
  ),
  Brazil: toDataUri(
    coffeeCardSvg({
      title: 'Brazil',
      bg1: '#F97316',
      bg2: '#F43F5E',
    }),
  ),
  Kenya: toDataUri(
    coffeeCardSvg({
      title: 'Kenya',
      bg1: '#22C55E',
      bg2: '#14B8A6',
    }),
  ),
}

