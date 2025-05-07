import React from 'react';
import Footer from '../../components/Footer';
import Nav from '../../components/Nav';
import '../../stylesheets/General.css';
import '../../stylesheets/Author.css';
import EmptySpace from '../../components/EmptySpace';
import BookDetailsCard from '../../components/BookDetailsCard';
import AuthorDetails from '../../components/AuthorDetails';


function AdminPage() {
  return (
    <body>
        <Nav />
        
        <EmptySpace />

        <div className='container white-box col-sm-10 p-3'>
            <div className='col-sm-6'>
                <h5><i class="bi bi-person-circle"></i> Sana Navaei </h5>
                <br/>
                <h5> <i class="bi bi-envelope"></i> sana.sarinavaei@gmail.com </h5>
            </div>
            
            <div className='text-end'>
                <button className="btn col-sm-2 gray-btn ">Logout</button>
            </div>
        </div>

        <div className='container col-sm-10'>
            <div className='row justify-content-center'>

                <button className='btn green-btn col-sm-2'>Add Author</button>
                <div className='col-sm-1'>  </div>
                <button className='btn green-btn col-sm-2'>Add Book</button>
            </div>
        </div>

        <div className="cart-box container white-box col-sm-10">
                <h4 className="mb-4 ms-1 fw-bold"><i class="bi bi-book"></i> Books</h4>
                <table className="table">
                    <thead className="table-light">
                    <tr>
                        <th>Image</th>
                        <th>Name</th>
                        <th>Author</th>
                        <th>Genre</th>
                        <th>Publisher</th>
                        <th>Published Year</th>
                        <th>Price</th>
                        <th>Total Buys</th>
                    </tr>
                    </thead>
                    <tbody>
                    <BookDetailsCard />
                    <BookDetailsCard />
                    </tbody>
                </table>
            </div>


            <div className="cart-box container white-box col-sm-10">
                <h4 className="mb-4 ms-1 fw-bold"><i class="bi bi-person-lines-fill"></i> Authors</h4>
                <table className="table">
                    <thead className="table-light">
                    <tr>
                        <th>Image</th>
                        <th>Name</th>
                        <th>Pen Name</th>
                        <th>Nationality</th>
                        <th>Born</th>
                        <th>Died</th>
                    </tr>
                    </thead>
                    <tbody>
                        <AuthorDetails />
                        <AuthorDetails />
                    </tbody>
                </table>
            </div>


        <Footer />

    </body>
  );
}

export default AdminPage;