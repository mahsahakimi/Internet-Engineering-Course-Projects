import React from 'react';

const StarRatingRenderer = ({ rating = 0}) => {
    const fullStars = Math.floor(rating);
    const hasHalfStar = rating - fullStars >= 0.25 && rating - fullStars < 0.75;
    const emptyStars = 5 - fullStars - (hasHalfStar ? 1 : 0);

    const stars = [];

    for (let i = 0; i < fullStars; i++) {
        stars.push(<i class="bi bi-star-fill"></i>);
    }

    if (hasHalfStar) {
        stars.push(<i class="bi bi-star-half"></i>);
    }

    for (let i = 0; i < emptyStars; i++) {
        stars.push(<i class="bi bi-star"></i>);
    }

    return <div className="flex gap-1">{stars}</div>;
};

export default StarRatingRenderer;