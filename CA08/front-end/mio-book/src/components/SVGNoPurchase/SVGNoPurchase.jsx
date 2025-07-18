function SVGNoPurchase(){
    return(
        <svg viewBox="0 0 200 200" xmlns="http://www.w3.org/2000/svg" width="240" height="240">
            <rect width="200" height="200" fill="#ffffff"/>

            <g>
                <rect x="60" y="50" width="80" height="100" rx="6" fill="#f5f5f5" stroke="#ccc" stroke-width="2"/>
                <line x1="65" y1="65" x2="135" y2="65" stroke="#ccc" stroke-width="1"/>
                <line x1="65" y1="75" x2="135" y2="75" stroke="#ccc" stroke-width="1"/>
                <line x1="65" y1="85" x2="135" y2="85" stroke="#ccc" stroke-width="1"/>
                <line x1="65" y1="95" x2="135" y2="95" stroke="#ccc" stroke-width="1"/>
            </g>

            <circle cx="85" cy="115" r="3" fill="#999"/>
            <circle cx="115" cy="115" r="3" fill="#999"/>
            <path d="M90 125 q10 -5 20 0" stroke="#999" stroke-width="2" fill="none"/>

            <circle cx="150" cy="40" r="12" fill="#bbb"/>
            <text x="150" y="45" font-size="12" text-anchor="middle" fill="#fff" font-family="Arial" font-weight="bold">0</text>

            <circle cx="30" cy="30" r="2" fill="#eee"/>
            <circle cx="40" cy="35" r="1.5" fill="#ddd"/>
            <circle cx="170" cy="170" r="2" fill="#eee"/>
        </svg>

    );
}

export default SVGNoPurchase;