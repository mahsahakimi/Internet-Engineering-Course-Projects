import React from 'react';
import authorPNG from '../../../src/images/author.jpg';

function AuthorDetails({ author }) {
  const formatDate = (date) => {
    if (!date) return 'N/A';
    const { year, month, day } = date;
    return `${year || '?'}-${month || '?'}-${day || '?'}`;
  };

  return (
    <tr>
      <td>
        <img
          src={authorPNG}
          className="product-img"
          alt={author?.name || 'Author portrait'}
        />
      </td>
      <td>{author?.name || 'N/A'}</td>
      <td>{author?.penName || 'N/A'}</td>
      <td>{author?.nationality || 'N/A'}</td>
      <td>{formatDate(author?.born)}</td>
      <td>
        {author?.died
          ? formatDate(author.died)
          : 'Alive'}
      </td>
    </tr>
  );
}

export default AuthorDetails;