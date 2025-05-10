function SVGCartIsEmpty({size}){
    return(
        <svg viewBox="0 0 200 200" xmlns="http://www.w3.org/2000/svg" width="100%" height="100%" preserveAspectRatio="xMidYMid meet">
            <circle cx="100" cy="100" r="80" fill="#f5f5f5"/>
            <path d="M60 70 H140 L130 110 H70 L60 70 Z" fill="white" stroke="#e0e0e0" stroke-width="2"/>
            <circle cx="80" cy="115" r="6" fill="white" stroke="#e0e0e0" stroke-width="2"/>
            <circle cx="120" cy="115" r="6" fill="white" stroke="#e0e0e0" stroke-width="2"/>
            <circle cx="95" cy="87" r="2" fill="#b0b0b0"/>
            <circle cx="105" cy="87" r="2" fill="#b0b0b0"/>
            <path d="M95 95 Q100 90 105 95" stroke="#b0b0b0" stroke-width="1.5" fill="none" stroke-linecap="round"/>
            <line x1="60" y1="70" x2="50" y2="50" stroke="#e0e0e0" stroke-width="2" />
            <text x="100" y="40" text-anchor="middle" font-size="24" fill="#b0b0b0" font-family="Arial, sans-serif">?</text>
        </svg>
    );
}

export default SVGCartIsEmpty;