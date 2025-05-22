import React from 'react';
import designBook from '../../../src/images/designBook.jpg';

function AdminBookDetailsCard({ book }) {
  const {
    title = 'Untitled Book',
    author = 'Unknown Author',
    genre = [],
    publisher = 'Unknown Publisher',
    year = 'N/A',
    price = '0.00'
  } = book;

  const formattedGenres = Array.isArray(genre) && genre.length > 0
    ? genre.join(", ")
    : 'No genres listed';

  const formattedPrice = typeof price === 'number'
    ? `$${price.toFixed(2)}`
    : `$${parseFloat(price || 0).toFixed(2)}`;

  return (
    <tr>
      <td>
        <img
          src={designBook}
          className="product-img"
          alt={`Cover of ${title}`}
        />
      </td>
      <td>{title}</td>
      <td>{author}</td>
      <td>{formattedGenres}</td>
      <td>{publisher}</td>
      <td>{year}</td>
      <td>{formattedPrice}</td>
      <th>10</th>
    </tr>
  );
}

export default AdminBookDetailsCard;