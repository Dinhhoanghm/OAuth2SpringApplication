import React, { useState } from 'react';
import { Card } from '@/components/ui/card';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';

const RegisterPage = () => {
  const navigate = useNavigate();
  const [formData, setFormData] = useState({
    first_name: '',
    last_name: '',
    email: '',
    password: '',
  });
  const [errorMessage, setErrorMessage] = useState<string | null>(null);
  const [successMessage, setSuccessMessage] = useState<string | null>(null);

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    setFormData((prev) => ({ ...prev, [name]: value }));
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    try {
      // Send registration data to the backend
      const response = await axios.post('http://localhost:8080/register', formData);
      if (response.status === 200) {
        alert(response.data);
        setErrorMessage(null);
        navigate('/login');
      }
    } catch (error) {
      console.log('error: ', error);
      if (axios.isAxiosError(error) && error.response) {
        setErrorMessage(error.response.data.message || 'Registration failed. Please try again.');
        setSuccessMessage(null);
      } else {
        setErrorMessage('An unexpected error occurred. Please try again.');
        setSuccessMessage(null);
      }
    }
  };

  return (
    <div className="min-h-screen flex items-center justify-center bg-gray-50">
      <Card className="w-full max-w-md p-8">
        <div className="text-center mb-8">
          <h1 className="text-3xl font-bold text-gray-900">Register</h1>
          <p className="text-gray-500 mt-2">Create an account to get started</p>
        </div>

        <form onSubmit={handleSubmit}>
          <div className="space-y-4">
            <input
              type="text"
              name="firstName"
              placeholder="First name"
              className="block w-full border border-gray-300 rounded-md px-4 py-2 text-sm focus:outline-none focus:ring-1 focus:ring-blue-500"
              value={formData.firstName}
              onChange={handleChange}
              required
            />
            <input
              type="text"
              name="lastName"
              placeholder="Last name"
              className="block w-full border border-gray-300 rounded-md px-4 py-2 text-sm focus:outline-none focus:ring-1 focus:ring-blue-500"
              value={formData.lastName}
              onChange={handleChange}
              required
            />
            <input
              type="email"
              name="email"
              placeholder="Email"
              className="block w-full border border-gray-300 rounded-md px-4 py-2 text-sm focus:outline-none focus:ring-1 focus:ring-blue-500"
              value={formData.email}
              onChange={handleChange}
              required
            />
            <input
              type="password"
              name="password"
              placeholder="Password"
              className="block w-full border border-gray-300 rounded-md px-4 py-2 text-sm focus:outline-none focus:ring-1 focus:ring-blue-500"
              value={formData.password}
              onChange={handleChange}
              required
            />
          </div>

          {errorMessage && <p className="text-sm text-red-500 mt-4">{errorMessage}</p>}
          {successMessage && <p className="text-sm text-green-500 mt-4">{successMessage}</p>}

          <button
            type="submit"
            className="w-full bg-blue-600 text-white py-2 px-4 rounded-md text-sm hover:bg-blue-700 mt-6"
          >
            Sign up
          </button>
        </form>

        <div className="mt-8 text-center">
          <a href="/login" className="text-sm text-blue-600 hover:text-blue-500 font-medium">
            Back to sign in
          </a>
        </div>
      </Card>
    </div>
  );
};

export default RegisterPage;
