import authorPNG from '../../../src/images/author.jpg';
import { Link } from 'react-router-dom';

function AuthorDetails({ author }) {
const formatDate = (date) => {
    if (!date) return 'N/A';
    const { year, month, day } = date;
    return `${year || '?'}-${month || '?'}-${day || '?'}`;
};

return (
    <tr>
    <td>
        <img
        src={authorPNG}
        className="product-img"
        alt={author?.name || 'Author portrait'}
        />
    </td>
    <td><Link to={`/author?name=${encodeURIComponent(author?.name)}`} className="link-text">
                {author?.name}
        </Link></td>
    <td>{author?.penName}</td>
    <td>{author?.nationality}</td>
    <td>{formatDate(author?.born)}</td>
    <td>
        {author?.died
        ? formatDate(author.died)
        : 'Alive'}
    </td>
    </tr>
);
}

export default AuthorDetails;