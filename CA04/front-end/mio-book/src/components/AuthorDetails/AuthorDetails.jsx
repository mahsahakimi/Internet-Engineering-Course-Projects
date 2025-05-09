import React from 'react';
import authorPNG from '../../../src/images/author.jpg';

function AuthorDetails({author}) {
  return (
      <tr>
          <td><img src={authorPNG} className="product-img" /></td>
          <td>{author?.name}</td>
          <td>{author?.penName}</td>
          <td>{author?.nationality}</td>
          <td>{author?.born?.year}-{author?.born?.month}-{author?.born?.day}</td>
          <td> {author?.died
                ? `${author.died.year}-${author.died.month}-${author.died.day}`
                : "alive"}
          </td>

      </tr>
  );
};

export default AuthorDetails;