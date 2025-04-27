import React from 'react';
import Footer from '../../components/Footer';
import Nav from '../../components/Nav';
import '../../stylesheets/General.css';
import '../../stylesheets/Author.css';
import SVGPersonCircle from '../../components/SVGPersonCircle';
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
                <h5>
                    <SVGPersonCircle/>
                    Sana Navaei
                </h5>
                <br/>
                <h5>
                    <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" fill="currentColor" class="bi bi-envelope" viewBox="0 0 16 16">
                        <path d="M0 4a2 2 0 0 1 2-2h12a2 2 0 0 1 2 2v8a2 2 0 0 1-2 2H2a2 2 0 0 1-2-2zm2-1a1 1 0 0 0-1 1v.217l7 4.2 7-4.2V4a1 1 0 0 0-1-1zm13 2.383-4.708 2.825L15 11.105zm-.034 6.876-5.64-3.471L8 9.583l-1.326-.795-5.64 3.47A1 1 0 0 0 2 13h12a1 1 0 0 0 .966-.741M1 11.105l4.708-2.897L1 5.383z"/>
                    </svg>
                    sana.sarinavaei@gmail.com
                </h5>
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
                <h4 className="mb-4 ms-1 fw-bold">
                    <svg xmlns="http://www.w3.org/2000/svg" width="28" height="28" fill="currentColor" class="bi bi-book" viewBox="0 0 16 16">
                        <path d="M1 2.828c.885-.37 2.154-.769 3.388-.893 1.33-.134 2.458.063 3.112.752v9.746c-.935-.53-2.12-.603-3.213-.493-1.18.12-2.37.461-3.287.811zm7.5-.141c.654-.689 1.782-.886 3.112-.752 1.234.124 2.503.523 3.388.893v9.923c-.918-.35-2.107-.692-3.287-.81-1.094-.111-2.278-.039-3.213.492zM8 1.783C7.015.936 5.587.81 4.287.94c-1.514.153-3.042.672-3.994 1.105A.5.5 0 0 0 0 2.5v11a.5.5 0 0 0 .707.455c.882-.4 2.303-.881 3.68-1.02 1.409-.142 2.59.087 3.223.877a.5.5 0 0 0 .78 0c.633-.79 1.814-1.019 3.222-.877 1.378.139 2.8.62 3.681 1.02A.5.5 0 0 0 16 13.5v-11a.5.5 0 0 0-.293-.455c-.952-.433-2.48-.952-3.994-1.105C10.413.809 8.985.936 8 1.783"/>
                    </svg>
                    Books
                </h4>
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
                <h4 className="mb-4 ms-1 fw-bold">
                    <svg xmlns="http://www.w3.org/2000/svg" width="28" height="29" fill="currentColor" class="bi bi-person-lines-fill" viewBox="0 0 16 16">
                        <path d="M6 8a3 3 0 1 0 0-6 3 3 0 0 0 0 6m-5 6s-1 0-1-1 1-4 6-4 6 3 6 4-1 1-1 1zM11 3.5a.5.5 0 0 1 .5-.5h4a.5.5 0 0 1 0 1h-4a.5.5 0 0 1-.5-.5m.5 2.5a.5.5 0 0 0 0 1h4a.5.5 0 0 0 0-1zm2 3a.5.5 0 0 0 0 1h2a.5.5 0 0 0 0-1zm0 3a.5.5 0 0 0 0 1h2a.5.5 0 0 0 0-1z"/>
                    </svg>
                    Authors
                </h4>
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