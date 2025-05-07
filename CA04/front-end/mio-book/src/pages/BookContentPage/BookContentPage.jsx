import React, { useState, useEffect } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import Footer from '../../components/Footer';
import Nav from '../../components/Nav';
import EmptySpace from '../../components/EmptySpace';
import '../../stylesheets/General.css';
import BookContentHeader from '../../components/BookContentHeader';
import BookContentText from '../../components/BookContentText';

function BookContentPage() {
  const location = useLocation();
  const navigate = useNavigate();
  const [bookData, setBookData] = useState(null);
  const [bookDetails, setBookDetails] = useState(null);
  const [loading, setLoading] = useState(true);
  const queryParams = new URLSearchParams(location.search);
  const bookTitleFromURL = queryParams.get('title');

  useEffect(() => {
    const fetchBookData = async () => {
      setLoading(true);
      setBookData(null);
      setBookDetails(null);

      if (!bookTitleFromURL) {
        console.error('Book title is missing from the URL.');
        setLoading(false);
        return;
      }

      try {
        // Fetch book content
        const contentResponse = await fetch(
          `http://localhost:8080/api/books/content?title=${encodeURIComponent(bookTitleFromURL)}`,
          {
            headers: {
              'Content-Type': 'application/json',
            },
          }
        );

        if (!contentResponse.ok) {
            const contentResponseJson = await contentResponse.json();
            navigate('/error', { state: { message: contentResponseJson.message } });
            return;
        }

        const contentResponseJson = await contentResponse.json();
        console.log('Content API Response:', contentResponseJson);

        if (contentResponseJson.status === 'success' && contentResponseJson.data) {
          try {
            const parsedContentData = JSON.parse(contentResponseJson.data);
            setBookData(parsedContentData);
          } catch (parseError) {
            console.error('Error parsing book content data:', parseError);
            navigate('/error', { state: { message: 'Failed to parse book content.' } });
            return;
          }
        } else {
          navigate('/error', { state: { message: contentResponseJson.message || 'Failed to retrieve book content.' } });
          return;
        }

        // Fetch book details for author
        const detailsResponse = await fetch(
          `http://localhost:8080/api/books/details?title=${encodeURIComponent(bookTitleFromURL)}`,
          {
            headers: {
              'Content-Type': 'application/json',
            },
          }
        );

        if (!detailsResponse.ok) {
          console.warn(`Failed to fetch book details: ${detailsResponse.status}. Author will not be displayed.`);
          // We don't want to block showing the content if details fail.
          return;
        }

        const detailsResponseJson = await detailsResponse.json();
        console.log('Details API Response:', detailsResponseJson);

        if (detailsResponseJson.status === 'success' && detailsResponseJson.data) {
          try {
            const parsedDetailsData = JSON.parse(detailsResponseJson.data);
            setBookDetails(parsedDetailsData);
          } catch (parseError) {
            console.error('Error parsing book details data:', parseError);
            // We don't want to block showing the content if details parsing fails.
          }
        } else {
          console.warn(
            detailsResponseJson.message || 'Failed to retrieve book details. Author will not be displayed.'
          );
          // We don't want to block showing the content if details retrieval fails.
        }
      } catch (err) {
        console.error('Error fetching data:', err);
        navigate('/error', { state: { message: 'Failed to load book data.' } });
      } finally {
        setLoading(false);
      }
    };

    fetchBookData();
  }, [bookTitleFromURL, navigate]);

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

  if (!bookData) {
    return null;
  }

  const authorName = bookDetails?.author?.name;

  return (
    <body>
      <Nav />
      <EmptySpace />
      <BookContentHeader title={bookData.title} author={authorName} />
      <EmptySpace />
      <BookContentText content={bookData.content} />
      <Footer />
    </body>
  );
}

export default BookContentPage;