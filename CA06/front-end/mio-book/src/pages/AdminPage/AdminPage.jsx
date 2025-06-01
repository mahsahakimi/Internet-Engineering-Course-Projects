import React, { useState, useContext, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import Footer from '../../components/Footer';
import Nav from '../../components/Nav';
import '../../stylesheets/General.css';
import '../../stylesheets/Author.css';
import EmptySpace from '../../components/EmptySpace';
import AdminBookDetailsCard from '../../components/AdminBookDetailsCard';
import AuthorDetails from '../../components/AuthorDetails';
import ModalAddAuthor from '../../components/ModalAddAuthor';
import ModalAddBook from '../../components/ModalAddBook';
import { UserContext } from '../../UserContex';

function AdminPage() {
    const [showBookModal, setShowBookModal] = useState(false);
    const [showAddAuthorModal, setShowAddAuthorModal] = useState(false);
    const [books, setBooks] = useState([]);
    const [authors, setAuthors] = useState([]);
    const [loadingBooks, setLoadingBooks] = useState(true);
    const [loadingAuthors, setLoadingAuthors] = useState(true);

    const { user, setUser } = useContext(UserContext);
    if(!user){
        const localUser = JSON.parse(localStorage.getItem('activeUser'));
        setUser(localUser)
    }

    const navigate = useNavigate();

    useEffect(() => {
        if (user?.userInfo?.role === "CUSTOMER") {
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
                navigate('/error', { state: { message: 'Logout failed.' } });
            }
        } catch (error) {
            console.error('Error while logging out:', error);
            navigate('/error', { state: { message: 'Error occurred during logout.' } });
        }
    };

    const fetchBooks = async () => {
        setLoadingBooks(true);
        try {
            const response = await fetch('/api/books/adminPageBooks');
            const responseData = await response.json();

            if (responseData.status === "success") {
                setBooks(responseData.data);
                
            } else {
                console.error("Failed to fetch books for admin page.");
                navigate('/error', { state: { message: responseData.message || "Failed to fetch books." } });
            }
        } catch (error) {
            console.error("Error fetching books for admin:", error);
            navigate('/error', { state: { message: "Failed to load books." } });
        } finally {
            setLoadingBooks(false);
        }
    };

    const fetchAuthors = async () => {
        setLoadingAuthors(true);
        try {
            const response = await fetch('/api/authors/getAll');
            const responseData = await response.json();
            
            if (responseData.status === "success") {
                setAuthors(responseData.data);
            } else {
                console.error("Failed to fetch authors.");
                navigate('/error', { state: { message: responseData.message || "Failed to fetch authors." } });
            }
        } catch (error) {
            console.error("Error fetching authors:", error);
            navigate('/error', { state: { message: "Failed to load authors." } });
        } finally {
            setLoadingAuthors(false);
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
                    <h5><i className="bi bi-person-circle"></i> {user?.userInfo?.username} </h5>
                    <br/>
                    <h5> <i className="bi bi-envelope"></i> {user?.userInfo?.email} </h5>
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
                    <h4 className="mb-4 ms-1 fw-bold"><i className="bi bi-book"></i> Books</h4>
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
                        {loadingBooks ? (
                            <tr><td colSpan="8">Loading books...</td></tr>
                        ) : books.length > 0 ? (
                            books.map((book, index) => (
                                    <AdminBookDetailsCard book={book} key={index} />
                            ))
                        ) : (
                            <tr><td colSpan="8">No books available.</td></tr>
                        )}
                        </tbody>
                    </table>
                </div>

                <EmptySpace />

                <div className="cart-box container white-box col-sm-10">
                    <h4 className="mb-4 ms-1 fw-bold"><i className="bi bi-person-lines-fill"></i> Authors</h4>
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
                        {loadingAuthors ? (
                            <tr><td colSpan="6">Loading authors...</td></tr>
                        ) : authors.length > 0 ? (
                            authors.map((author, index) => (
                                    <AuthorDetails author={author} key={index} />
                            ))
                        ) : (
                            <tr><td colSpan="6">No authors available.</td></tr>
                        )}
                        </tbody>
                    </table>
                </div>

            <ModalAddAuthor show={showAddAuthorModal} onClose={() => setShowAddAuthorModal(false)} onSubmit={() => {setShowBookModal(false); fetchAuthors();}}/>
            <ModalAddBook show={showBookModal} onClose={() => setShowBookModal(false)} onSubmit={() => {setShowBookModal(false); fetchBooks();}}
/>

            <Footer />

        </>
    );
}

export default AdminPage;