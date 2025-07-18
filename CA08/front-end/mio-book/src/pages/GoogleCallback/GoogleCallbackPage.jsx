import React, { useEffect, useContext } from 'react';
import { useNavigate, useLocation } from 'react-router-dom';
import { UserContext } from '../../UserContex';

function GoogleCallbackPage() {
    const navigate = useNavigate();
    const { setUser } = useContext(UserContext);
    const location = useLocation();
    const queryParams = new URLSearchParams(location.search);
    const codeFromURL = queryParams.get('code');

    useEffect(() => {
        if (!codeFromURL) {
            console.error("No code found in URL.");
            // navigate('/signup');
            return;
        }
        fetchToken(codeFromURL);
    }, []);

    const fetchToken = async (code) => {
        const controller = new AbortController();
        const timeoutId = setTimeout(() => controller.abort(), 5000);

        try {
            const response = await fetch('api/auth/google-code', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                signal: controller.signal,
                body: JSON.stringify({ "code" : code })
            });

            clearTimeout(timeoutId);

            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }

            const responseData = await response.json();

            const activeUser = { userInfo: responseData.data };
            setUser(activeUser);
            localStorage.setItem('activeUser', JSON.stringify(activeUser));
            
            navigate('/');

        } catch (error) {
            navigate("/error", { state: { message: "injaam  " + error.message } });
        }
    };

    return (
    <div className="d-flex justify-content-center align-items-center vh-100">
        <h2>Logging you in... Please wait.</h2>
    </div>
    );
}

export default GoogleCallbackPage;