import React from 'react';
import designBook from '../../../src/images/designBook.jpg';

function BookDetailsCard({book}) {
  return (
      <tr>
          <td><img src={designBook} className="product-img" /></td>
          <td>{book?.title}</td>
          <td>{book?.author}</td>
          <td>{book?.genre?.join(", ")}</td>
          <td>{book?.publisher}</td>
          <td>{book?.year}</td>
          <td>{book?.price}</td>
          <th>{10}</th>
      </tr>
  );
};

export default BookDetailsCard;