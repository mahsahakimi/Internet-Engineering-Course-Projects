import React, {useEffect} from 'react';
import { BrowserRouter, Routes, Route } from 'react-router-dom';
import 'bootstrap-icons/font/bootstrap-icons.css';
import { UserProvider } from './UserContex.jsx';
import SignInPage from './pages/SignInPage';
import SignUpPage from './pages/SignUpPage';
import HomePage from './pages/HomePage';
import SearchResult from './pages/SearchResult';
import BookPage from './pages/BookPage';
import AuthorPage from './pages/AuthorPage';
import BookContentPage from './pages/BookContentPage';
import UserPage from './pages/UserPage';
import BuyCartPage from './pages/BuyCartPage';
import ErrorPage from './pages/ErrorPage';
import Error404Page from './pages/Error404Page';
import AdminPage from './pages/AdminPage';
import ErrorBoundary from './components/Error/ErrorBoundary.jsx';
import ProtectedRoute from './ProtectedRoute.jsx';


function App() {
  // useEffect(() => {
  //   localStorage.clear();
  // }, []);

  return (
    <BrowserRouter>
      <UserProvider>
        <ErrorBoundary>
          <Routes>
            <Route path="/" element={<ProtectedRoute><HomePage /></ProtectedRoute>} />
            <Route path="/signin" element={<SignInPage />} />
            <Route path="/signup" element={<SignUpPage />} />
            <Route path="/results" element={<ProtectedRoute><SearchResult /></ProtectedRoute>} />
            <Route path="/book-details" element={<ProtectedRoute><BookPage /></ProtectedRoute>} />
            <Route path="/author" element={<ProtectedRoute><AuthorPage /></ProtectedRoute>} />
            <Route path="/book/content" element={<ProtectedRoute><BookContentPage /></ProtectedRoute>} />
            <Route path="/user" element={<ProtectedRoute><UserPage /></ProtectedRoute>} />
            <Route path="/cart" element={<ProtectedRoute><BuyCartPage /></ProtectedRoute>} />
            <Route path="/admin" element={<ProtectedRoute><AdminPage /></ProtectedRoute>} />
            <Route path="/error" element={<ErrorPage />} />
            <Route path="*" element={<Error404Page />} />
          </Routes>
        </ErrorBoundary>
      </UserProvider>
    </BrowserRouter>
  );
}

export default App;