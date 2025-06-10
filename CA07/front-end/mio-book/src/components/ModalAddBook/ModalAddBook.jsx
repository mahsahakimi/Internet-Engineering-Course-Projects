import React, { useContext, useState } from 'react';
 import { useNavigate } from 'react-router-dom';
 import '../../stylesheets/Modal.css';
import { UserContext } from '../../UserContex';

 const ModalAddBook = ({ show, onClose, onSubmit }) => {
    const { user, setUser } = useContext(UserContext);
    const [step, setStep] = useState(1);
    const [formData, setFormData] = useState({
        title: '',
        author: '',
        publisher: '',
        genres: '',
        year: '',
        price: '',
        imageLink: '',
        synopsis: '',
        content: ''
    });
    const navigate = useNavigate();

    const isStepOneComplete = formData.title && formData.author && formData.publisher && formData.genres && formData.year && formData.price && formData.imageLink;
    const isStepTwoComplete = formData.synopsis && formData.content;

    const handleChange = (e) => {
        setFormData({ ...formData, [e.target.name]: e.target.value });
    };

    const handleNext = () => setStep(2);
    const handleBack = () => setStep(1);

    const handleClose = () => {
        setStep(1);
        setFormData({
            title: '',
            author: '',
            publisher: '',
            genres: '',
            year: '',
            price: '',
            imageLink: '',
            synopsis: '',
            content: ''
        });
        onClose();
    };

     const handleSubmit = async () => {
         try {
             const response = await fetch('/api/books/add', {
                 method: 'POST',
                 headers: { 
                    'Authorization': `Bearer ${user.userInfo.token}`,
                    'Content-Type': 'application/json'
                 },
                 body: JSON.stringify(formData),
             });

             if (!response.ok) {
                 console.error(`Failed to add book: ${response.status}`);
                 const responseData = await response.json();
                 navigate('/error', { state: { message: responseData.message } });
                 return;
             }

             const data = await response.json();
             if (data.status === 'success') {
                 onSubmit();
             } else {
                  console.error(data.message || 'Failed to add book.');
                 navigate('/error', { state: { message: data.message || 'Failed to add book.' } });
             }
         } catch (error) {
             console.error('Submission error:', error);
             navigate('/error', { state: { message: 'An error occurred while adding the book.' } });
         }
         handleClose();
     };

     if (!show) return null;

     return (
         <>
             <div className="modal fade show d-block" tabIndex="-1" role="dialog">
                 <div className="modal-dialog modal-dialog-centered" role="document">
                     <div className="modal-content">
                         <div className="modal-header d-flex justify-content-between">
                             <div className="flex-grow-1 text-center">
                                 <h6 className="modal-title">Add Book</h6>
                             </div>
                             <button type="button" className="btn-close" aria-label="Close" style={{ transform: 'scale(0.8)' }} onClick={handleClose}></button>
                         </div>

                         <div className="modal-body">
                             {step === 1 ? (
                                 <>
                                     <input className="form-control mb-2" placeholder="Name" name="title" value={formData.title} onChange={handleChange} />
                                     <input className="form-control mb-2" placeholder="Author" name="author" value={formData.author} onChange={handleChange} />
                                     <input className="form-control mb-2" placeholder="Publisher" name="publisher" value={formData.publisher} onChange={handleChange} />
                                     <input className="form-control mb-2" placeholder="Genres" name="genres" value={formData.genres} onChange={handleChange} />
                                     <input className="form-control mb-2" placeholder="Published Year" name="year" value={formData.year} onChange={handleChange} />
                                     <input className="form-control mb-2" placeholder="Price" name="price" value={formData.price} onChange={handleChange} />
                                     <input className="form-control mb-2" placeholder="Image Link" name="imageLink" value={formData.imageLink} onChange={handleChange} />
                                 </>
                             ) : (
                                 <>
                                     <textarea className="form-control mb-3" rows="4" placeholder="Synopsis" name="synopsis" value={formData.synopsis} onChange={handleChange} />
                                     <textarea className="form-control mb-3" rows="6" placeholder="Content" name="content" value={formData.content} onChange={handleChange} />
                                 </>
                             )}
                         </div>

                         <div className="d-flex flex-column align-items-center w-100 px-3 mb-3">
                             {step === 1 ? (
                                 <button
                                     className={`btn ${isStepOneComplete ? 'green-btn' : 'gray-btn'} mb-2 col-12`}
                                     onClick={handleNext}
                                     disabled={!isStepOneComplete}
                                 >
                                     Next
                                 </button>
                             ) : (
                                 <>
                                     <button
                                         className={`btn ${isStepTwoComplete ? 'green-btn' : 'gray-btn'} mb-2 col-12`}
                                         onClick={handleSubmit}
                                         disabled={!isStepTwoComplete}
                                     >
                                         Submit
                                     </button>
                                     <button className="btn gray-btn col-12" onClick={handleBack}>
                                         Back
                                     </button>
                                 </>
                             )}
                             <button className="btn gray-btn col-12 mt-2" onClick={handleClose}>
                                 Cancel
                             </button>
                         </div>
                     </div>
                 </div>
             </div>
             <div className="modal-backdrop fade show"></div>
         </>
     );
 };

 export default ModalAddBook;