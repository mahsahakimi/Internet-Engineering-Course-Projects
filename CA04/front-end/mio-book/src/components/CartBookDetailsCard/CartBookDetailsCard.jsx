import React from 'react';
import designBook from '../../../public/designBook.jpg';

function CartBookDetailsCard() {
  return (
      <tr>
          <td><img src={designBook} className="product-img"/></td>
          <td>The Design of Books</td>
          <td>Debbie Berne</td>
          <td>$ 18.00</td>
          <td>Not Borrowed</td>
          <th>
              <button className="btn gray-btn col-sm-12">Remove</button>
          </th>
      </tr>
  );
};

export default CartBookDetailsCard;