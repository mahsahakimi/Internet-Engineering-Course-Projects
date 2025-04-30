import React, { useState , useContext } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { UserContext } from '../../UserContex';
import '../../stylesheets/SignInPage.css';
import '../../stylesheets/General.css';
import Footer from '../../components/Footer';
import EmptySpace from '../../components/EmptySpace';
import SVGEyeSlash from '../../components/SVGEyeSlash';
import SVGEyeIcon from '../../components/SVGEyeIcon';

function SignInPage() {
  // Model
  const [formData, setFormData] = useState({
    username: '',
    password: '',
  });

  const [errors, setErrors] = useState({});
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [showPassword, setShowPassword] = useState(false);
  const { setUser } = useContext(UserContext);
  const navigate = useNavigate();

  // Controller
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

  const togglePasswordVisibility = () => {
    setShowPassword(!showPassword);
  };

  const validateForm = () => {
    const newErrors = {};
    let isValid = true;

    if (!formData.username.trim()) {
      newErrors.username = 'Username is required';
      isValid = false;
    }

    if (!formData.password) {
      newErrors.password = 'Password is required';
      isValid = false;
    }

    setErrors(newErrors);
    return isValid;
  };

  const handleUserLogin = async (e) => {
    e.preventDefault();

    if (validateForm()) {
      setIsSubmitting(true);
      setErrors({});
      
      try {
        const response = await fetch('/api/login', {
          method: 'POST',
          headers: {  
            'Content-Type': 'application/json',
          },
          body: JSON.stringify({
            username: formData.username,
            password: formData.password,
          }),
        });

        const responseData = await response.json();

        if (response.ok && response.status === 200) {
          const activeUser = { userInfo: responseData.data };

          // SAVE the response data into a file
          // const blob = new Blob([JSON.stringify(activeUser, null, 2)], { type: 'text/plain' });
          // const url = URL.createObjectURL(blob);
          // const a = document.createElement('a');
          // a.href = url;
          // a.download = 'backend_response.txt'; // file name
          // a.click();
          // URL.revokeObjectURL(url);

          setUser(activeUser);
          localStorage.setItem('activeUser', JSON.stringify(activeUser));
          navigate('/');
          console.log(responseData.message); // "User logged in successfully."
        } 
        else {
          if (responseData && responseData.message) {
            setErrors({ general: responseData.message });
          } else {
            setErrors({ general: 'Login failed. Please check your credentials.' });
          }      
        }
      } catch (error) {
        console.error('Login error:', error);
        setErrors({ general: 'Failed to connect to the server.' });
      } finally {
        setIsSubmitting(false);
      }
    }
  };

  const isFormFilled = () => {
    return formData.username.trim() && formData.password;
  };

  // View
  return (
    <div className="justify-content-center">
      <EmptySpace />
      <div className="row">
        <div className="container white-box col-sm-6">
          <h2 className="text-center">Login</h2>
          <p className="subtitle text-center mb-5">MioBook</p>
          <div className="row justify-content-center">
            <form onSubmit={handleUserLogin} className="col-10 col-sm-8">
              {/* Username Field */}
              <div className="mb-3">
                <input
                  type="text"
                  className={`form-control ${errors.username ? 'is-invalid' : ''}`}
                  name="username"
                  placeholder="Username"
                  value={formData.username}
                  onChange={handleChange}
                />
              </div>

              {/* Password Field */}
              <div className="mb-3">
                <div className="input-group">
                  <input
                    type={showPassword ? 'text' : 'password'}
                    className={`form-control ${errors.password ? 'is-invalid' : ''}`}
                    name="password"
                    placeholder="Password"
                    value={formData.password}
                    onChange={handleChange}
                  />
                  <button
                    className="btn gray-btn"
                    type="button"
                    onClick={togglePasswordVisibility}
                  >
                    {showPassword ? ( <SVGEyeSlash/> ) : (<SVGEyeIcon/>)}
                  </button>

                </div>
              </div>
              <div>
                {errors.general && <div className="text-danger">{errors.general}</div>}
              </div>

              {/* Login Button */}
              <button
                type="submit"
                className="btn gray-btn mb-3 col-12"
                disabled={!isFormFilled() || isSubmitting}
              >
                {isSubmitting ? 'Logging In...' : <strong>Log In</strong>}
              </button>
            </form>
          </div>

          {/* Sign Up Link */}
          <p className="signin-link text-center">
            Don't have an account?
            <Link to="/signup">
              <strong>Sign up</strong>
            </Link>
          </p>
        </div>
      </div>
      <Footer />
    </div>
  );
}

export default SignInPage;