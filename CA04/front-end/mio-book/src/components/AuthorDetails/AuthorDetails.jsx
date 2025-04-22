import React from 'react';
import author from '../../../src/images/author.jpg';

function AuthorDetails() {
  return (
      <tr>
          <td><img src={author} className="product-img" /></td>
          <td>Stephen King</td>
          <td>DRichard Bachman</td>
          <td>American</td>
          <td>1947-09-21</td>
          <td>-</td>

      </tr>
  );
};

export default AuthorDetails;