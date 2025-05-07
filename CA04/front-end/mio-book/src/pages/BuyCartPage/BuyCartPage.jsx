import React from 'react';
import Footer from '../../components/Footer';
import Nav from '../../components/Nav';
import CartBookDetailsCard from '../../components/CartBookDetailsCard';
import '../../stylesheets/General.css';
import EmptySpace from '../../components/EmptySpace';


function BuyCartPage() {
  return (
    <body>
        <Nav />
        <EmptySpace />
        <div className="row justify-content-center">
            <div className="container white-box col-sm-10">
                <h4 className="mb-4 ms-1 fw-bold">
                    <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" fill="currentColor" className="icon" viewBox="0 1 16 16">
                        <path d="M0 1.5A.5.5 0 0 1 .5 1H2a.5.5 0 0 1 .485.379L2.89 3H14.5a.5.5 0 0 1 .491.592l-1.5 8A.5.5 0 0 1 13 12H4a.5.5 0 0 1-.491-.408L2.01 3.607 1.61 2H.5a.5.5 0 0 1-.5-.5z"/>
                        <path d="M3.102 4l1.313 7h8.17l1.313-7H3.102zM5 12a2 2 0 1 0 0 4 2 2 0 0 0 0-4zm7 0a2 2 0 1 0 0 4 2 2 0 0 0 0-4zm-7 1a1 1 0 1 1 0 2 1 1 0 0 1 0-2zm7 0a1 1 0 1 1 0 2 1 1 0 0 1 0-2z"/>
                    </svg>
                    Cart
                </h4>
                <table className="table">
                    <thead className="table-light">
                    <tr>
                        <th>Image</th>
                        <th>Name</th>
                        <th>Author</th>
                        <th>Price</th>
                        <th>Borrow Days</th>
                        <th> </th>
                    </tr>
                    </thead>
                    <tbody>
                    <CartBookDetailsCard />
                    <CartBookDetailsCard />
                    </tbody>
                </table>
                <div className="d-flex justify-content-center">
                    <button className="btn col-sm-4 green-btn">Purchase</button>
                </div>
            </div>
        </div>

        <Footer />
    </body>
  );
};

export default BuyCartPage;