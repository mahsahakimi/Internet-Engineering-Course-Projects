import React , { useContext, useState, useEffect } from 'react';
import Footer from '../../components/Footer';
import Nav from '../../components/Nav';
import UserBookDetailsCard from '../../components/UserBookDetailsCard';
import '../../stylesheets/UserPage.css';
import '../../stylesheets/General.css';
import EmptySpace from '../../components/EmptySpace';
import { useNavigate } from 'react-router-dom';
import { UserContext } from '../../UserContex';


function UserPage() {

    const [formData, setFormData] = useState({
        amount: '',
    });
    const [errors, setErrors] = useState({});
    const [books, setBooks] = useState([]);
    const [balance, setBalance] = useState([]);
    const { user, setUser } = useContext(UserContext);
    const localUser = JSON.parse(localStorage.getItem('activeUser'));
    if(!user){
        setUser(localUser)
    }
    
    const navigate = useNavigate();
    useEffect(() => {
        if (user?.userInfo?.role === "admin") {
            navigate("/admin", { replace: true });
            return;
        }

    }, [user, navigate]);

    const fetchUserBooks = async () => {

        try {
            const response = await fetch('/api/users/purchased_books');
            const responseData = await response.json();
            if (responseData.status === "success") {
                setBooks(responseData.data.books);
                setBalance(responseData.data.balance);
            } else {
                console.error("Failed to fetch Cart details.");
                navigate('/error', { state: { message: responseData.message || "Failed to fetch Cart details." } });
            }
        } catch (error) {
            console.error("Error fetching Cart details:", error);
            navigate('/error', { state: { message: error.message } });
        }
    };

    useEffect(() => {
        if (user?.userInfo?.role === "ADMIN") {
            navigate("/admin", { replace: true });
            return;
        }
        fetchUserBooks();
    }, []);


    const handleChange = (e) => {    
        const { name, value } = e.target;
        setFormData({
            ...formData,
            [name]: value,
        });
        if (errors[name]) {
            setErrors({
                ...errors,
                [name]: null,
            });
        }
        
    };

    const validateForm = () => {
        const newErrors = {};
        let isValid = true;
    
        if (!formData.amount.trim()) {
            newErrors.amount = 'Enter a value';
            isValid = false;
        } else if (isNaN(Number(formData.amount))) {
            newErrors.amount = 'Enter a valid number';
            isValid = false;
        } else if (Number(formData.amount) <= 0) {
            newErrors.amount = 'Amount must be greater than zero';
            isValid = false;
        }
    
        setErrors(newErrors);
        return isValid;
    };
    

    const handleLogout = async () => {
        try{
            const response = await fetch('/api/logout', {
                method: 'POST',
                // credentials: 'include', // Important if your backend uses cookies/session
            });
            
            if(response.ok){
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

    const handleAddCredit = async () => {
        if (validateForm()) {
            setErrors({});
            try{
                const response = await fetch('/api/users/addcredit', {
                    method: 'POST',
                    headers: {  
                        'Content-Type': 'application/json',
                    },
                    body: JSON.stringify({
                        credit: formData.amount,
                    }),
                });
                
                const responseData = await response.json();
                if(responseData.status === "success"){
                    setBalance(responseData.data);
                    setFormData({ amount: '' });
                } else {
                    const newErrors = {};
                    newErrors.amount = responseData.message;
                    setErrors(newErrors);
                    console.error('Failed :', responseData.message);
                }
            } catch (error) {
                const newErrors = {};
                newErrors.amount = error;
                setErrors(newErrors);
                console.error('Error while adding credit:', error);
            }
        }
    }

    return (
        <div>
            <Nav />
            <EmptySpace />
            <div className="row justify-content-center">
                <div className="row col-sm-10">
                    <div className="container white-box col-sm-8">
                        <div className="m-3"><h3> <i class="bi bi-coin"></i> {balance}</h3></div>
                        <div className="d-flex">
                        <input 
                            type="text" 
                            name="amount"
                            value={formData.amount}
                            onChange={handleChange}
                            className={`form-control me-2 ${errors.amount ? 'is-invalid' : ''}`}
                            placeholder="$Amount"
                        />
                            <button 
                            className="btn col-sm-3 gray-btn"
                            onClick={handleAddCredit}
                            >
                                Add more credit
                            </button>
                        </div>
                        {errors.amount && <div className="text-danger">{errors.amount}</div>}
                    </div>
                    <div className="col-sm-1"></div>
                    <div className="user-info container white-box col-sm-3">
                        <p><strong><i class="bi bi-person-circle"></i> {user?.userInfo?.username} </strong></p>
                        <p><i class="bi bi-envelope"></i> {user?.userInfo?.email} </p>
                        <button  onClick={handleLogout} className="btn gray-btn col-sm-6">logout</button>
                    </div>
                </div>
                <EmptySpace />
                <div className="container white-box col-sm-10">
                    <h4 className="mb-4 ms-1 fw-bold"> <i class="bi bi-book"></i> My Books </h4>

                    {books && (books?.owned?.length > 0 || books?.borrowed?.length > 0) ? (
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
                                <th></th>
                            </tr>
                        </thead>
                        <tbody>
                            {books?.owned?.map((book, index) => (
                                <UserBookDetailsCard book={book} status="owned"  />
                            ))}
                            {books?.borrowed?.map((book, index) => (
                                <UserBookDetailsCard book={book} status="borrowed" />
                            ))}
                        </tbody>
                    </table>
                ) : (
                    <div className="empty-state">
                        <svg className="empty-illustration" viewBox="0 0 200 200" xmlns="http://www.w3.org/2000/svg" preserveAspectRatio="xMidYMid meet" width="100%" height="100%">
                            <circle cx="100" cy="100" r="80" fill="#f5f5f5" />
                            <path d="M70 70 H130 A4 4 0 0 1 134 74 V126 A4 4 0 0 1 130 130 H70 A4 4 0 0 1 66 126 V74 A4 4 0 0 1 70 70 Z" 
                                fill="white" stroke="#e0e0e0" strokeWidth="2" />
                            <line x1="85" y1="70" x2="85" y2="130" stroke="#e0e0e0" strokeWidth="2" />
                            <line x1="90" y1="85" x2="125" y2="85" stroke="#e0e0e0" strokeWidth="1" />
                            <line x1="90" y1="95" x2="125" y2="95" stroke="#e0e0e0" strokeWidth="1" />
                            <line x1="90" y1="105" x2="125" y2="105" stroke="#e0e0e0" strokeWidth="1" />
                            <circle cx="95" cy="115" r="2" fill="#b0b0b0" />
                            <circle cx="115" cy="115" r="2" fill="#b0b0b0" />
                            <path d="M98 122 Q105 115 112 122" stroke="#b0b0b0" strokeWidth="1.5" fill="none" strokeLinecap="round" />
                            <path d="M138 68 Q138 64 142 64 Q145 64 145 68 Q145 70 142 71 Q140 72 140 74" stroke="#e0e0e0" strokeWidth="1.5" fill="none" />
                            <circle cx="140" cy="77" r="1.5" fill="#e0e0e0" />
                        </svg>
                        <p className="empty-message">No book available!</p>
                    </div>
                )}

                    
                </div>
            </div>

            <Footer />

        </div>
    );
};

export default UserPage;