import React, { useState , useContext } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { UserContext } from '../../UserContex';
import '../../stylesheets/SignUpPage.css';
import '../../stylesheets/General.css';
import Footer from '../../components/Footer';
import EmptySpace from '../../components/EmptySpace';
import SVGEyeIcon from '../../components/SVGEyeIcon';
import SVGEyeSlash from '../../components/SVGEyeSlash';

function SignUpPage() {
  // Model
  const [formData, setFormData] = useState({
    username: '',
    password: '',
    email: '',
    country: '',
    city: '',
    role: 'customer'
  });

  const [errors, setErrors] = useState({});
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [showPassword, setShowPassword] = useState(false);
  const { setUser } = useContext(UserContext);
  const navigate = useNavigate();

  // Mock database of existing users (in a real app, this would be an API call)
  const [existingUsers, setExistingUsers] = useState([]); // Initialize as empty

  // Controller
  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({
      ...formData,
      [name]: value
    });

    // Clear error when user starts typing
    if (errors[name]) {
      setErrors({
        ...errors,
        [name]: null
      });
    }
  };

  const handleRoleChange = (role) => {
    setFormData({
      ...formData,
      role
    });
  };

  const togglePasswordVisibility = () => {
    setShowPassword(!showPassword);
  };

  const validateForm = () => {
    const newErrors = {};
    let isValid = true;

    // Username validation
    if (!formData.username.trim()) {
      newErrors.username = 'Username is required';
      isValid = false;
    }

    // Email validation
    if (!formData.email.trim()) {
      newErrors.email = 'Email is required';
      isValid = false;
    } else if (!/^\S+@\S+\.\S+$/.test(formData.email)) {
      newErrors.email = 'Invalid email format';
      isValid = false;
    }

    // Password validation
    if (!formData.password) {
      newErrors.password = 'Password is required';
      isValid = false;
    } else if (formData.password.length < 4) {
      newErrors.password = 'Password must be at least 4 characters';
      isValid = false;
    }

    // Location validation
    if (!formData.country.trim()) {
      newErrors.country = 'Country is required';
      isValid = false;
    }
    if (!formData.city.trim()) {
      newErrors.city = 'City is required';
      isValid = false;
    }

    setErrors(newErrors);
    return isValid;
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    if (validateForm()) {
      setIsSubmitting(true);
      setErrors({}); // Clear previous errors before submitting

      try {
        const response = await fetch('/api/users/add', {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
          },
          body: JSON.stringify({
            role: formData.role,
            username: formData.username,
            password: formData.password,
            email: formData.email,
            address: {
              country: formData.country,
              city: formData.city,
            },
          }),
        });
        
        const responseData = await response.json();

        if (response.ok && response.status === 201) {
          const new_response = await fetch('/api/login', {
            method: 'POST',
            headers: {
              'Content-Type': 'application/json',
            },
            body: JSON.stringify({
              username: formData.username,
              password: formData.password,
            }),
          });

          const responseData = await new_response.json();

          if (new_response.ok && new_response.status === 200){
            const loggedUser = { userInfo: responseData.data };
            setUser(loggedUser);
            localStorage.setItem('logged_user', JSON.stringify(loggedUser));
            navigate('/');
          }
          else {
            navigate('/signin')
          }
        } 
        // if error occures
        else { 
          if (responseData.status == "error") {           
              if(responseData.message.toLowerCase().includes("username")){
                setErrors({ username: responseData.message });
              }
              if(responseData.message.toLowerCase().includes("password")){
                setErrors({ password: responseData.message });
              }
              if(responseData.message.toLowerCase().includes("email")){
                setErrors({ email: responseData.message });
              }
          }
          else if (responseData && responseData.message) {
            // If the backend sends a general 'message'
            setErrors({ general: responseData.message });
          } else {
            setErrors({ general: 'Registration failed. Please try again.' });
          }
        }
      } catch (error) {
        console.error('Registration failed:', error);
        setErrors({ general: error.message });
      } finally {
        setIsSubmitting(false);
      }
    }
  };

  // View
  const isFormValid = () => {
    return (
      formData.username.trim() &&
      formData.password &&
      formData.email.trim() &&
      formData.country.trim() &&
      formData.city.trim() &&
      Object.keys(errors).length === 0
    );
  };

  return (
    <div className="justify-content-center">
      <EmptySpace />
      <div className="row">
        <div className="container white-box col-sm-6">
          <h2 className="text-center">Sign Up</h2>
          <p className="subtitle text-center mb-5">MioBook</p>
          <div className="row justify-content-center">
            <form onSubmit={handleSubmit} className="col-10 col-sm-8">
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

              {/* Email Field */}
              <div className="mb-3">
                <input
                  type="email"
                  className={`form-control ${errors.email ? 'is-invalid' : ''}`}
                  name="email"
                  placeholder="Email"
                  value={formData.email}
                  onChange={handleChange}
                />
                {errors.email && <div className="invalid-feedback text-danger">{errors.email}</div>}
              </div>

              {/* Location Fields */}
              <div className="row mb-3">
                <div className="col">
                  <input
                    type="text"
                    className={`form-control ${errors.country ? 'is-invalid' : ''}`}
                    name="country"
                    placeholder="Country"
                    value={formData.country}
                    onChange={handleChange}
                  />
                  {errors.country && <div className="invalid-feedback text-danger">{errors.country}</div>}
                </div>
                <div className="col">
                  <input
                    type="text"
                    className={`form-control ${errors.city ? 'is-invalid' : ''}`}
                    name="city"
                    placeholder="City"
                    value={formData.city}
                    onChange={handleChange}
                  />
                  {errors.city && <div className="invalid-feedback text-danger">{errors.city}</div>}
                </div>
              </div>

              {/* Role Selection */}
              <label>I am</label>
              <div className="row mb-3 mt-3">
                <div className="col-6">
                  <button
                    type="button"
                    className={`btn role-btn col-12 ${formData.role === 'customer' ? 'selected' : ''}`}
                    onClick={() => handleRoleChange('customer')}
                  >
                    <i></i> Customer
                  </button>
                </div>
                <div className="col-6">
                  <button
                    type="button"
                    className={`btn role-btn col-12 ${formData.role === 'manager' ? 'selected' : ''}`}
                    onClick={() => handleRoleChange('manager')}
                  >
                    <i></i> Manager
                  </button>
                </div>
              </div>

              {/* Submit Button */}
              <button
                type="submit"
                className="btn gray-btn mb-3 col-12"
                disabled={!isFormValid() || isSubmitting}
              >
                {isSubmitting ? 'Processing...' : <strong>Sign up</strong>}
              </button>
            </form>
          </div>

          {/* Sign In Link */}
          <p className="signin-link text-center">
            Already have an account?
            <Link to="/signin">
              <strong>Sign in</strong>
            </Link>
          </p>
        </div>
      </div>
      <Footer />
    </div>
  );
}

export default SignUpPage;