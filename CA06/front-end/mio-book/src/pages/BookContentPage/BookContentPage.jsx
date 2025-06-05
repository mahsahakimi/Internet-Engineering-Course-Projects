import { useState, useEffect, useContext } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import Footer from '../../components/Footer';
import Nav from '../../components/Nav';
import EmptySpace from '../../components/EmptySpace';
import '../../stylesheets/General.css';
import BookContentHeader from '../../components/BookContentHeader';
import BookContentText from '../../components/BookContentText';
import { UserContext } from '../../UserContex';

function BookContentPage() {
  const { user, setUser } = useContext(UserContext);
  const location = useLocation();
  const navigate = useNavigate();
  const [bookData, setBookData] = useState(null);
  const [loading, setLoading] = useState(true);
  const queryParams = new URLSearchParams(location.search);
  const bookTitleFromURL = queryParams.get('title');

  const fetchBookData = async () => {
    setLoading(true);
    setBookData(null);

    if (!bookTitleFromURL) {
      console.error('Book title is missing from the URL.');
      setLoading(false);
      return;
    }

    try {
      const contentResponse = await fetch(
        `/api/books/content?title=${encodeURIComponent(bookTitleFromURL)}`,
        {
          headers: {
            'Session-ID': user.userInfo.sessionId,
            'Content-Type': 'application/json',
          },
        }
      );

      const content = await contentResponse.json();
      if (!contentResponse.ok) {
          navigate('/error', { state: { message: content.message } });
          return;
      }

      if (content.status === 'success' && content.data) {
          setBookData(content.data);
      } else {
        navigate('/error', { state: { message: content.message || 'Failed to retrieve book content.' } });
        return;
      }

    } catch (err) {
      console.error('Error fetching data:', err);
      navigate('/error', { state: { message: 'Failed to load book data.' } });
    } finally {
      setLoading(false);
    }
  };


  useEffect(() => {
    if (!user) return;
    fetchBookData();
  }, [bookTitleFromURL, navigate, user]);

  if (loading) {
    return (
      <body>
        <Nav />
        <EmptySpace />
        <div className='container col-sm-10 white-box'>
          <p>Loading book content...</p>
        </div>
        <Footer />
      </body>
    );
  }

  return (
    <body>
      <Nav />
      <EmptySpace />
      <BookContentHeader title={bookData.title} author={bookData.author} />
      <EmptySpace />
      <BookContentText content={bookData.content} />
      <Footer />
    </body>
  );
}

export default BookContentPage;