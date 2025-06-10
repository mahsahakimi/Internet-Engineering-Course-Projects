import React, { useContext, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import '../../stylesheets/Modal.css';
import { UserContext } from '../../UserContex';

const ModalAddAuthor = ({ show, onClose, onSubmit }) => {
  const { user, setUser } = useContext(UserContext);
  const [formData, setFormData] = useState({
    name: '',
    penName: '',
    nationality: '',
    born: '',
    died: '',
    image: '',
  });
  const navigate = useNavigate();

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({ ...prev, [name]: value }));
  };

  const handleClose = () => {
    setFormData({ name: '', penName: '', nationality: '', born: '', died: '', image: '' });
    onClose();
  };

  const handleSubmit = async () => {
    try {
      const response = await fetch('/api/authors/add', {
        method: 'POST',
        headers: { 
          'Authorization': `Bearer ${user.userInfo.token}`,
          'Content-Type': 'application/json'
         },
        body: JSON.stringify(formData),
      });

      if (!response.ok) {
        console.error(`Failed to add author: ${response.status}`);
        const responseData = await response.json();
        navigate('/error', { state: { message: responseData.message } });
        return;
      }

      const result = await response.json();
      if (result.status === 'success') {
        onSubmit();
      } else {
        console.error(result.message || 'Failed to add author.');
        navigate('/error', { state: { message: result.message || 'Failed to add author.' } });
      }
    } catch (err) {
      console.error('Submission error:', err);
      navigate('/error', { state: { message: err.message } });
    }
    handleClose();
  };

  const isFormComplete = formData.name && formData.penName && formData.nationality && formData.born && formData.image;

  if (!show) return null;

  return (
    <>
      <div className="modal fade show d-block" tabIndex="-1" role="dialog">
        <div className="modal-dialog modal-dialog-centered" role="document">
          <div className="modal-content">
            <div className="modal-header d-flex justify-content-between">
              <div className="flex-grow-1 text-center">
                <h5 className="modal-title">Add Author</h5>
              </div>
              <button type="button" className="btn-close" aria-label="Close" style={{ transform: 'scale(0.8)' }} onClick={handleClose}></button>
            </div>

            <div className="modal-body">
              <input name="name" type="text" className="form-control mb-2" placeholder="Name" value={formData.name} onChange={handleChange} />
              <input name="penName" type="text" className="form-control mb-2" placeholder="Pen Name" value={formData.penName} onChange={handleChange} />
              <input name="nationality" type="text" className="form-control mb-2" placeholder="Nationality" value={formData.nationality} onChange={handleChange} />
              <input name="born" type="date" className="form-control mb-2" placeholder="Born" value={formData.born} onChange={handleChange} />
              <input name="died" type="date" className="form-control mb-2" placeholder="Died" value={formData.died} onChange={handleChange} />
              <input name="image" type="text" className="form-control mb-2" placeholder="Image Link" value={formData.image} onChange={handleChange} />
            </div>

            <div className="d-flex flex-column align-items-center w-100">
            <button
                className={`btn ${isFormComplete ? 'green-btn' : 'gray-btn'} mb-3 col-12`}
                onClick={handleSubmit}
                disabled={!isFormComplete}
                >
                Submit
            </button>

              <button className="btn gray-btn col-12" onClick={handleClose}>Cancel</button>
            </div>
          </div>
        </div>
      </div>
      <div className="modal-backdrop fade show"></div>
    </>
  );
};

export default ModalAddAuthor;