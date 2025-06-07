import React, { useState, useEffect, useContext } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import Footer from '../../components/Footer';
import Nav from '../../components/Nav';
import BookCard from '../../components/BookCard';
import AuthorInfo from '../../components/AuthorInfo';
import AuthorHeader from '../../components/AuthorHeader';
import '../../stylesheets/General.css';
import '../../stylesheets/Author.css';
import EmptySpace from '../../components/EmptySpace';
import { UserContext } from '../../UserContex';

function AuthorPage() {
  const { user, setUser } = useContext(UserContext);
  const location = useLocation();
  const queryParams = new URLSearchParams(location.search);
  const authorNameFromURL = queryParams.get('name');
  const [authorDetails, setAuthorDetails] = useState(null);
  const [authorBooks, setAuthorBooks] = useState([]);
  const [loading, setLoading] = useState(true);
  const navigate = useNavigate();
  

  useEffect(() => {
    if (!user) return;
    fetchAuthorData();
  }, [authorNameFromURL, navigate, user]);

  const fetchAuthorData = async () => {
    setLoading(true);
    setAuthorDetails(null);
    setAuthorBooks([]);

    if (!authorNameFromURL) {
      console.error('Author name is missing from the URL.');
      setLoading(false);
      return;
    }

    try {
      const detailsResponse = await fetch(
        `/api/authors/details?username=${encodeURIComponent(authorNameFromURL)}`,
        {
          method: 'GET',
          headers: {
            'Session-ID': user.userInfo.sessionId
          }
        }
      );

      const responseData = await detailsResponse.json();
      if (!detailsResponse.ok) {
        console.error(`Failed to fetch author details: ${detailsResponse.status}`);
        navigate('/error', { state: { message: responseData.message } });
        return;
      }

      if (responseData.status === 'success') {
        try {
          setAuthorDetails(responseData.data);
        } catch (parseError) {
          console.error('Error parsing author details:', parseError);
          navigate('/error', { state: { message: 'Failed to parse author details.' } });
          return;
        }
      } else {
        navigate('/error', { state: { message: responseData.message || 'Failed to retrieve author details.' } });
        return;
      }

      // Fetch author's books
      const response = await fetch(
        `/api/books/search_by_author?author=${encodeURIComponent(authorNameFromURL)}&page=1&pageSize=10`,
        {
          method: 'GET',
          headers: {
            'Session-ID': user.userInfo.sessionId
          }
        }
      );

      if (!response.ok) {
        console.warn(`Failed to fetch author's books: ${response.status}`);
        navigate('/error', { state: { message: `Failed to fetch author's books: ${response.status}` } });
        return;
      }

      const newResponseData = await response.json();

      if (newResponseData.status === 'success') {
        try {
          setAuthorBooks(newResponseData.data);
        } catch (parseError) {
          console.error('Error parsing author books:', parseError);
          navigate('/error', { state: { message: 'Failed to parse author books.' } });
          return;
        }
      } else {
        console.warn(newResponseData.message || 'Failed to retrieve author\'s books.');
        navigate('/error', { state: { message: newResponseData.message || 'Failed to retrieve author\'s books.' } });
        return;
      }
    } catch (err) {
      console.error('Error fetching data:', err);
      navigate('/error', { state: { message: err.message } });
    } finally {
      setLoading(false);
    }
  };

  if (loading) {
    return (
      <>
        <Nav />
        <div className="container mt-5">
          <p>Loading author details and books...</p>
        </div>
        <Footer />
      </>
    );
  }

  if (!authorDetails) {
    return (
      <>
        <Nav />
        <div className="container mt-5">
          <p>Could not load author details.</p>
        </div>
        <Footer />
      </>
    );
  }

  return (
    <>
      <Nav />

      <AuthorHeader name={authorDetails.name} />

      <EmptySpace />
      <EmptySpace />

      <AuthorInfo author={authorDetails} bookCount={authorBooks?.length || 0} />
      <EmptySpace />

      <section className="container mb-5">
        <div className="row row-cols-1 row-cols-sm-5">
          {(authorBooks || []).map((book) => (
            <div className="col" key={book._id}>
              <BookCard book={book} />
            </div>
          ))}
        </div>
      </section>

      <Footer />
    </>
  );

}

export default AuthorPage;