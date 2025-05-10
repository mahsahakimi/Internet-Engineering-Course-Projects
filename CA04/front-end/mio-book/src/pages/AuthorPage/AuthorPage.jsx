import React, { useState, useEffect } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import Footer from '../../components/Footer';
import Nav from '../../components/Nav';
import BookCard from '../../components/BookCard';
import AuthorInfo from '../../components/AuthorInfo';
import AuthorHeader from '../../components/AuthorHeader';
import '../../stylesheets/General.css';
import '../../stylesheets/Author.css';
import EmptySpace from '../../components/EmptySpace';

function AuthorPage() {
  const location = useLocation();
  const queryParams = new URLSearchParams(location.search);
  const authorNameFromURL = queryParams.get('name');
  const [authorDetails, setAuthorDetails] = useState(null);
  const [authorBooks, setAuthorBooks] = useState([]);
  const [loading, setLoading] = useState(true);
  const navigate = useNavigate();

  useEffect(() => {
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
          `/api/authors/details?username=${encodeURIComponent(authorNameFromURL)}`
        );

        if (!detailsResponse.ok) {
          console.error(`Failed to fetch author details: ${detailsResponse.status}`);
          const responseData = await response.json();
          navigate('/error', { state: { message: responseData.message } });
          return;
        }

        const detailsResponseJson = await detailsResponse.json();
        console.log('Author Details API Response:', detailsResponseJson);

        if (detailsResponseJson.status === 'success' && detailsResponseJson.data) {
          try {
            const parsedDetailsData = JSON.parse(detailsResponseJson.data);
            setAuthorDetails(parsedDetailsData);
          } catch (parseError) {
            console.error('Error parsing author details:', parseError);
            navigate('/error', { state: { message: 'Failed to parse author details.' } });
            return;
          }
        } else {
          navigate('/error', { state: { message: detailsResponseJson.message || 'Failed to retrieve author details.' } });
          return;
        }

        // Fetch author's books
        const booksResponse = await fetch(
          `/api/books/search_by_author?name=${encodeURIComponent(
            authorNameFromURL
          )}&page=1&pageSize=10`
        );

        if (!booksResponse.ok) {
          console.warn(`Failed to fetch author's books: ${booksResponse.status}`);
          navigate('/error', { state: { message: `Failed to fetch author's books: ${booksResponse.status}` } });
          return;
        }

        const booksResponseJson = await booksResponse.json();
        console.log('Author Books API Response:', booksResponseJson);

        if (booksResponseJson.status === 'success' && booksResponseJson.data) {
          try {
            const parsedBooksData = JSON.parse(booksResponseJson.data);
            setAuthorBooks(parsedBooksData);
          } catch (parseError) {
            console.error('Error parsing author books:', parseError);
            navigate('/error', { state: { message: 'Failed to parse author books.' } });
            return;
          }
        } else {
          console.warn(booksResponseJson.message || 'Failed to retrieve author\'s books.');
          navigate('/error', { state: { message: booksResponseJson.message || 'Failed to retrieve author\'s books.' } });
          return;
        }
      } catch (err) {
        console.error('Error fetching data:', err);
        navigate('/error', { state: { message: 'Failed to load author data.' } });
      } finally {
        setLoading(false);
      }
    };

    fetchAuthorData();
  }, [authorNameFromURL, navigate]);

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

          <AuthorInfo author={authorDetails} bookCount={authorBooks.length} />
          <EmptySpace />

          <section className="container mb-5">
              <div className="row row-cols-1 row-cols-sm-5">
              {authorBooks.map((book) => (
                  <div className="col" key={book._id}> {/* Added a key for mapping */}
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