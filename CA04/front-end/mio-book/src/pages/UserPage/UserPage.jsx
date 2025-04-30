import React , { useContext, useState } from 'react';
import Footer from '../../components/Footer';
import Nav from '../../components/Nav';
import BookDetailsCard from '../../components/BookDetailsCard';
import '../../stylesheets/UserPage.css';
import '../../stylesheets/General.css';
import EmptySpace from '../../components/EmptySpace';
import { useNavigate } from 'react-router-dom';
import { UserContext } from '../../UserContex';
import SVGPersonCircle from '../../components/SVGPersonCircle';
import SVGMail from '../../components/SVGMail';
import SVGBook from '../../components/SVGBook';
import SVGCoin from '../../components/SVGCoin';

function UserPage() {
    const [formData, setFormData] = useState({
        amount: '',
    });
    const [errors, setErrors] = useState({});
    const { user, setUser } = useContext(UserContext);
    const navigate = useNavigate();
    const localUser = JSON.parse(localStorage.getItem('activeUser'));
    if(!user){
        setUser(localUser)
    }

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
                    const newBalance = responseData.data;

                    const currentUser = user || JSON.parse(localStorage.getItem('activeUser'));
                    const updatedUser = {
                        ...currentUser,
                        userInfo: {
                            ...currentUser.userInfo,
                            balance: newBalance,
                        },
                    };
                    
                    setUser(updatedUser);
                    localStorage.setItem('activeUser', JSON.stringify(updatedUser));
    
                } else {
                    // const newErrors = {};
                    // newErrors.amount = responseData.message;
                    // setErrors(newErrors);
                    console.error('Failed :', responseData.message);
                }
            } catch (error) {
                // const newErrors = {};
                // newErrors.amount = error;
                // setErrors(newErrors);
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
                        <div className="m-3"><h3> <SVGCoin size="28"/> {user?.userInfo?.balance}</h3></div>
                        <div className="d-flex">
                        <input 
                            type="number" 
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
                        <p>
                            <strong><SVGPersonCircle size="20"/> {user?.userInfo?.username} </strong>
                            </p>
                        <p>
                            <SVGMail size="20"/> {user?.userInfo?.email}
                            </p>
                        <button  onClick={handleLogout} className="btn gray-btn col-sm-6">logout</button>
                    </div>
                </div>
                <EmptySpace />
                <div className="container white-box col-sm-10">
                    <h4 className="mb-4 ms-1 fw-bold"> <SVGBook size="32" /> My Books </h4>
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