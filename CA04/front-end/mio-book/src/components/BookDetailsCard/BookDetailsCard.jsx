import React from 'react';
import designBook from '../../../public/designBook.jpg';

function BookDetailsCard() {
  return (
      <tr>
          <td><img src={designBook} className="product-img" /></td>
          <td>The Design of Books</td>
          <td>Debbie Berne</td>
          <td>Guide</td>
          <td>The Publishers</td>
          <td>2024</td>
          <td>Owned</td>
          <th>
              <button className="btn gray-btn col-sm-12">Read</button>
          </th>
      </tr>
  );
};

export default BookDetailsCard;