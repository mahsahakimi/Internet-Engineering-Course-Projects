import React , { useContext, useState } from 'react';
import Footer from '../../components/Footer';
import Nav from '../../components/Nav';
import BookDetailsCard from '../../components/BookDetailsCard';
import '../../stylesheets/UserPage.css';
import '../../stylesheets/General.css';
import EmptySpace from '../../components/EmptySpace';
import { useNavigate } from 'react-router-dom';
import { UserContext } from '../../UserContex';

function UserPage() {
    const [formData, setFormData] = useState({
        amount: '',
    });

    const { user, setUser } = useContext(UserContext);
    const navigate = useNavigate();
    const localUser = !user ? JSON.parse(localStorage.getItem('logged_user')) : null;
    const displayUser = user || localUser;

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData({
            ...formData,
            [name]: value,
        });
    };

    const handleLogout = async () => {
        try{
            const response = await fetch('/api/logout', {
                method: 'POST',
                // credentials: 'include', // Important if your backend uses cookies/session
            });
            
            if(response.ok){
                setUser(null);
                localStorage.removeItem('logged_user');
                navigate('/signin');
            } else {
                console.error('Logout failed.');
            }
            
        } catch (error) {
            const blob = new Blob([JSON.stringify('error', null, 2)], { type: 'text/plain' });
            const url = URL.createObjectURL(blob);
            const a = document.createElement('a');
            a.href = url;
            a.download = 'backend_response.txt'; // file name
            a.click();
            URL.revokeObjectURL(url);
            console.error('Error while logging out:', error);
        }
    };

    return (
        <div>
            <Nav />
            <EmptySpace />
            <div className="row justify-content-center">
                <div className="row col-sm-10">
                    <div className="container white-box col-sm-8">
                        <div className="m-3"><h3>$  {displayUser?.userInfo?.balance}</h3></div>
                        <div className="d-flex">
                        <input 
                            type="number" 
                            name="amount"
                            value={formData.amount}
                            onChange={handleChange}
                            className="form-control me-2"
                            placeholder="$Amount"
                        />
                            <button className="btn col-sm-3 gray-btn">Add more credit</button>
                        </div>
                    </div>
                    <div className="col-sm-1"></div>
                    <div className="user-info container white-box col-sm-3">
                        <p>
                            <strong>👤 {displayUser?.userInfo?.username} </strong>
                            </p>
                        <p>
                            📧 {displayUser?.userInfo?.email}
                            </p>
                        <button  onClick={handleLogout} className="btn gray-btn col-sm-6">logout</button>
                    </div>
                </div>
                <EmptySpace />
                <div className="container white-box col-sm-10">
                    <h4 className="mb-4 ms-1 fw-bold">
                        <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" fill="currentColor" className="icon" viewBox="0 1 16 16">
                            <path d="M0 1.5A.5.5 0 0 1 .5 1H2a.5.5 0 0 1 .485.379L2.89 3H14.5a.5.5 0 0 1 .491.592l-1.5 8A.5.5 0 0 1 13 12H4a.5.5 0 0 1-.491-.408L2.01 3.607 1.61 2H.5a.5.5 0 0 1-.5-.5z"/>
                            <path d="M3.102 4l1.313 7h8.17l1.313-7H3.102zM5 12a2 2 0 1 0 0 4 2 2 0 0 0 0-4zm7 0a2 2 0 1 0 0 4 2 2 0 0 0 0-4zm-7 1a1 1 0 1 1 0 2 1 1 0 0 1 0-2zm7 0a1 1 0 1 1 0 2 1 1 0 0 1 0-2z"/>
                        </svg>
                        My Books
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
                            <th>Status</th>
                            <th> </th>
                        </tr>
                        </thead>
                        <tbody>
                        <BookDetailsCard />
                        <BookDetailsCard />
                        </tbody>
                    </table>
                </div>
            </div>

            <Footer />

        </div>
    );
};

export default UserPage;