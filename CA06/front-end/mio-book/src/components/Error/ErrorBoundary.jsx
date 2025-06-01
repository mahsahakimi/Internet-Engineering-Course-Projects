import React from 'react';
import { useNavigate } from 'react-router-dom';

class ErrorBoundary extends React.Component {
  constructor(props) {
    super(props);
    this.state = { hasError: false, error: null, errorInfo: null };
  }

  static getDerivedStateFromError(error) {
    // Update state so the next render will show the fallback UI.
    return { hasError: true, error };
  }

  componentDidCatch(error, errorInfo) {
    // Log the error to an error reporting service
    console.error('Caught an error:', error, errorInfo);
    this.setState({ errorInfo: errorInfo });
    // Programmatically redirect to the error page
    this.props.navigate('/error', { state: { message: error.toString() } });
  }

  render() {
    if (this.state.hasError) {
      // We won't render fallback UI here as we are redirecting
      return null;
    }

    return this.props.children;
  }
}

// Wrap with withRouter to get access to history for redirection
import { withRouter } from './withRouter'; // See helper function below
export default withRouter(ErrorBoundary);