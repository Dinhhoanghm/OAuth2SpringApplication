import { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';
import axiosInstance from '../axios';

function Failure() {
  const queryParams = new URLSearchParams(window.location.search);
  const sessionId = queryParams.get('session_id');
  const navigate = useNavigate();

  useEffect(() => {
    // If sessionId is present in the URL, call the API to process the payment result
    if (sessionId) {
      axiosInstance
        .get(`http://localhost:8080/api/v1/stripe/capture-payment?sessionId=${sessionId}`)
        .then((response) => {
          console.log('Payment process failed, handled successfully:', response.data);
          // Handle failure response
        })
        .catch((error) => {
          console.error('Error processing payment failure:', error);
        });
    } else {
      console.error('No sessionId found in the URL');
    }
  }, [sessionId]);

  const onButtonClick = () => {
    navigate('/dashboard');
  };

  return (
    <div className="min-h-screen flex justify-center items-center bg-red-100">
      <div className="bg-white p-8 rounded-lg shadow-lg max-w-md w-full text-center">
        <h1 className="text-4xl font-bold text-red-600">Failure!</h1>
        <p className="text-black mt-4 mb-6">Payment failed. Your session ID is: {sessionId}</p>
        <button
          onClick={onButtonClick}
          className="px-6 py-3 bg-red-500 text-white rounded-md hover:bg-red-600 focus:outline-none focus:ring-2 focus:ring-red-400"
        >
          Try Again
        </button>
      </div>
    </div>
  );
}

export default Failure;
