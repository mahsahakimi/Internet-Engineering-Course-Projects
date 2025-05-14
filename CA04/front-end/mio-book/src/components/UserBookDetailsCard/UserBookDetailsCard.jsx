    import React from 'react';
    import designBook from '../../../src/images/designBook.jpg';
    import { Link } from 'react-router-dom';


    function UserBookDetailsCard({ book, status }) {
    const {
        title = 'Untitled Book',
        author = 'Unknown Author',
        genres = [],
        publisher = 'Unknown Publisher',
        year = 'N/A',
        dueDate = null,
    } = book;

    const formattedGenres = Array.isArray(genres) && genres.length > 0
        ? genres.join(', ')
        : 'No genres listed';

    const formattedDueDate = dueDate ? new Date(dueDate).toLocaleString([], {
        year: 'numeric',
        month: '2-digit',
        day: '2-digit',
        hour: '2-digit',
        minute: '2-digit',
    }) : null;

    return (
        <tr>
        <td>
            <img
            src={designBook}
            className="product-img"
            alt={`Cover of ${title}`}
            />
        </td>
        <td><Link 
            to={`/book-details?title=${encodeURIComponent(title)}`} className="link-text">
            {title}
        </Link></td>
        <td><Link to={`/author?name=${encodeURIComponent(author)}`} className="link-text">
            {author}
        </Link></td>          
        <td>{formattedGenres}</td>
        <td>{publisher}</td>
        <td>{year}</td>
        <td>
            <div>
            <div>{status === 'owned' ? 'Owned' : 'Borrowed'}</div>
            {status === 'borrowed' && dueDate && (
                <div className="text-muted" style={{ fontSize: '0.85em' }}>
                Until {formattedDueDate}
                </div>
            )}
            </div>
        </td>
        <td> <button className='btn gray-btn'>Read</button></td>
        </tr>
    );
    }

    export default UserBookDetailsCard;
