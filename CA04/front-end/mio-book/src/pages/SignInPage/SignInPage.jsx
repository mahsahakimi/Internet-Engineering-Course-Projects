import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
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
        const response = await fetch('http://localhost:8080/api/login', {
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
          navigate('/');
          console.log(responseData.message); // "User logged in successfully."
        } 
        else {
            if (responseData.status == "error") {        
                if(responseData.message.toLowerCase().includes("username")){
                  setErrors({ username: responseData.message });
                }
                else if(responseData.message.toLowerCase().includes("password")){
                  setErrors({ password: responseData.message });
                }
                else{
                  setErrors({ general: responseData.message });
                }
            }
            else {
                // Handle login errors from the backend
                if (responseData && responseData.message) {
                  setErrors({ general: responseData.message });
                } else {
                  setErrors({ general: 'Login failed. Please check your credentials.' });
                }
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
              {errors.general && <div className="alert alert-danger">{errors.general}</div>}
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
                {errors.username && <div className="invalid-feedback text-danger">{errors.username}</div>}
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
                {errors.password && <div className="invalid-feedback text-danger">{errors.password}</div>}
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