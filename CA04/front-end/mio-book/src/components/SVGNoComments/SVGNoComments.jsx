function SVGNoComments({size}){
    return(
        <svg viewBox="0 0 200 200" xmlns="http://www.w3.org/2000/svg" width="200" height="200" preserveAspectRatio="xMidYMid meet">
            <circle cx="100" cy="100" r="80" fill="#f5f5f5"/>
        
            <path d="M60 70 H140 A10 10 0 0 1 150 80 V110 A10 10 0 0 1 140 120 H90 L70 135 L72 120 H60 A10 10 0 0 1 50 110 V80 A10 10 0 0 1 60 70 Z"
                fill="white" stroke="#e0e0e0" stroke-width="2"/>
        
            <circle cx="85" cy="95" r="2" fill="#b0b0b0"/>
            <circle cx="115" cy="95" r="2" fill="#b0b0b0"/>
            <path d="M88 105 Q100 98 112 105" stroke="#b0b0b0" stroke-width="1.5" fill="none" stroke-linecap="round"/>
        
            <text x="100" y="50" text-anchor="middle" font-size="20" fill="#ccc" font-family="Arial">?</text>
        </svg>
    );
}
export default SVGNoComments;







