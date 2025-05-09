import React, { useState, useContext, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import Footer from '../../components/Footer';
import Nav from '../../components/Nav';
import '../../stylesheets/General.css';
import '../../stylesheets/Author.css';
import EmptySpace from '../../components/EmptySpace';
import BookDetailsCard from '../../components/BookDetailsCard';
import AuthorDetails from '../../components/AuthorDetails';
import ModalAddAuthor from '../../components/ModalAddAuthor';
import ModalAddBook from '../../components/ModalAddBook';
import { UserContext } from '../../UserContex';




function AdminPage() {
    const [showBookModal, setShowBookModal] = useState(false);
    const [showAddAuthorModal, setShowAddAuthorModal] = useState(false);
    const [books, setBooks] = useState([]);
    const [authors, setAuthors] = useState([]);

    const { user, setUser } = useContext(UserContext);
    if(!user){
        const localUser = JSON.parse(localStorage.getItem('activeUser'));
        setUser(localUser)
    }

    const navigate = useNavigate();

    useEffect(() => {
        if (user?.userInfo?.role === "customer") {
            navigate("/user", { replace: true });
        }
    }, [user, navigate]);


    const handleLogout = async () => {
        try {
            const response = await fetch('/api/logout', {
                method: 'POST',
                // credentials: 'include', // Important if your backend uses cookies/session
            });

            if (response.ok) {
                setUser(null);
                localStorage.removeItem('activeUser');
                navigate('/signin');
            } else {
                console.error('Logout failed.');
            }
        } catch (error) {
            console.error('Error while logging out:', error);
        }
    };

    const fetchBooks = async () => {
        try {
            const response = await fetch('/api/books/adminPageBooks');
            const responseData = await response.json();
            
            if (responseData.status === "success") {
                setBooks(responseData.data);
            } else {
                console.error("Failed to fetch yop rated.");
            }
        } catch (error) {
            console.error("Error fetching top rated:", error);
        }
    };
    const fetchAuthors = async () => {
        try {
            const response = await fetch('/api/authors/getAll');
            const responseData = await response.json();
            
            if (responseData.status === "success") {
                setAuthors(responseData.data);
            } else {
                console.error("Failed to fetch yop rated.");
            }
        } catch (error) {
            console.error("Error fetching top rated:", error);
        }
    };

    useEffect(() => {
        fetchBooks();
        fetchAuthors();
    }, []);

    return (
        <>
            <Nav />
            
            <EmptySpace />

            <div className='container white-box col-sm-10 p-3'>
                <div className='col-sm-6'>
                    <h5><i class="bi bi-person-circle"></i> {user?.userInfo?.username} </h5>
                    <br/>
                    <h5> <i class="bi bi-envelope"></i> {user?.userInfo?.email} </h5>
                </div>
                
                <div className='text-end'>
                    <button onClick={handleLogout} className="btn gray-btn col-2">
                        Logout
                    </button>
                </div>
            </div>

            <div className='container col-sm-10'>
                <div className='row justify-content-center'>

                    <button className='btn green-btn col-sm-2' onClick={() => setShowAddAuthorModal(true)}>Add Author</button>
                    <div className='col-sm-1'>  </div>
                    <button className='btn green-btn col-sm-2' onClick={() => setShowBookModal(true)}>Add Book</button>
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
                        {books.length > 0 ? (
                            books.map((book, index) => (
                                    <BookDetailsCard book={book} />
                            ))
                        ) : (
                            <p>Loading...</p>
                        )}
                        </tbody>
                    </table>
                </div>

                <EmptySpace />

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
                        {authors.length > 0 ? (
                            authors.map((author, index) => (
                                    <AuthorDetails author={author} />
                            ))
                        ) : (
                            <p>Loading...</p>
                        )}
                        </tbody>
                    </table>
                </div>

            <ModalAddAuthor show={showAddAuthorModal} onClose={() => setShowAddAuthorModal(false)} />
            <ModalAddBook show={showBookModal} onClose={() => setShowBookModal(false)} onSubmit={() => {setShowBookModal(false);}}
/>

            <Footer />

        </>
    );
}

export default AdminPage;