import React from 'react';
import SVGStarFill from '../SVGStarFill';
import SVGStarHalfFill from '../SVGStarHalfFill';
import SVGStar from '../SVGStar';

const StarRatingRenderer = ({ rating = 0, size = 14 }) => {
    const fullStars = Math.floor(rating);
    const hasHalfStar = rating - fullStars >= 0.25 && rating - fullStars < 0.75;
    const emptyStars = 5 - fullStars - (hasHalfStar ? 1 : 0);

    const stars = [];

    for (let i = 0; i < fullStars; i++) {
        stars.push(<SVGStarFill key={`full-${i}`} size={size} />);
    }

    if (hasHalfStar) {
        stars.push(<SVGStarHalfFill key="half" size={size} />);
    }

    for (let i = 0; i < emptyStars; i++) {
        stars.push(<SVGStar key={`empty-${i}`} size={size} />);
    }

    return <div className="flex gap-1">{stars}</div>;
};

export default StarRatingRenderer;
