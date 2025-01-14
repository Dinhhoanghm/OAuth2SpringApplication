import { Card } from '@/components/ui/card';
import { useRef, useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';
import { useAuthStore } from '../lib/store';
import { RoleSelectionDialog } from '../components/auth/role-selection-dialog';

export default function LoginPage() {
  const formRef = useRef<HTMLFormElement>(null);
  const navigate = useNavigate();
  const [errorMessage, setErrorMessage] = useState<string | null>(null);
  const [showRoleDialog, setShowRoleDialog] = useState(false);
  const [pendingAdminUser, setPendingAdminUser] = useState<any>(null);
  const { login } = useAuthStore();

  const handleLoginResponse = (data: { email: string; role: string }) => {
    const { email, role } = data;

    switch (role) {
      case 'USER':
        window.location.href = 'http://localhost:8080/aiv';
        break;

      case 'SUPER_ADMIN': {
        const user = { id: '1', email, role: 'super_admin' };
        login(user);
        navigate('/clients', { replace: true });
        break;
      }

      case 'ADMIN': {
        const user = { id: '1', email, role: 'admin' };
        setPendingAdminUser(user);
        setShowRoleDialog(true);
        break;
      }

      default:
        setErrorMessage('Unknown role. Please contact support.');
    }
  };

  const handleRoleSelection = (choice: 'aiv' | 'subscription') => {
    setShowRoleDialog(false);

    if (choice === 'aiv') {
      window.location.href = 'http://localhost:8080/aiv';
    } else if (pendingAdminUser) {
      login(pendingAdminUser);
      navigate('/dashboard', { replace: true });
    }
    setPendingAdminUser(null);
  };
  const handleOAuthCallback = async () => {
    const urlParams = new URLSearchParams(window.location.search);

    // Extract all the required parameters
    const code = urlParams.get('code');
    const scope = urlParams.get('scope');
    const authuser = urlParams.get('authuser');
    const prompt = urlParams.get('prompt');
    const provider = urlParams.get('provider');
    console.log('code', code);

    if (!code) {
      setErrorMessage('Invalid OAuth response. Missing code or provider.');
      return;
    }
    let redirectUri = '';
    console.log('path', window.location.pathname);
    if (window.location.pathname.includes('/google')) {
      redirectUri = 'http://localhost:8080/grantcode';
    } else if (window.location.pathname.includes('/github')) {
      redirectUri = 'http://localhost:8080/github/grantcode';
    } else {
      setErrorMessage('Unable to determine the provider.');
      return;
    }

    console.log('redirect url', redirectUri);
    try {
      // Send all extracted parameters to the backend
      const response = await axios.get(redirectUri, {
        params: {
          code,
          scope,
          authuser,
          prompt,
          provider,
        },
      });

      // Debugging: Log the response
      console.log('OAuth Response:', response.data);
      localStorage.setItem('token', response.data.access_token);
      if (response.data?.user) {
        handleLoginResponse(response.data.user);
      } else {
        setErrorMessage('Invalid server response.');
      }
    } catch (error) {
      if (axios.isAxiosError(error) && error.response) {
        setErrorMessage(error.response.data.message || `${provider} login failed.`);
      } else {
        setErrorMessage('An unexpected error occurred during OAuth login.');
      }
    }
  };

  useEffect(() => {
    // Only handle OAuth callback if the URL includes a `code` parameter
    if (window.location.search.includes('code')) {
      handleOAuthCallback();
    }
  }, []);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    const emailInput = (document.querySelector('input[type="email"]') as HTMLInputElement)?.value;
    const passwordInput = (document.querySelector('input[type="password"]') as HTMLInputElement)?.value;

    if (!emailInput || !passwordInput) {
      setErrorMessage('Please enter both email and password.');
      return;
    }

    try {
      const response = await axios.post('http://localhost:8080/login', {
        username: emailInput,
        password: passwordInput,
      });

      if (response.data?.user) {
        localStorage.setItem('token', response.data.access_token);
        handleLoginResponse(response.data.user);
      } else {
        setErrorMessage('Invalid server response.');
      }
    } catch (error) {
      if (axios.isAxiosError(error) && error.response) {
        setErrorMessage(error.response.data.message || 'Invalid login credentials.');
      } else {
        setErrorMessage('An unexpected error occurred. Please try again.');
      }
    }
  };

  const initiateOAuth = (provider: string) => {
    const githubRedirectUri = 'http://localhost:5173/github';

    const authUrl =
      provider === 'google'
        ? `https://accounts.google.com/o/oauth2/v2/auth?redirect_uri=http://localhost:5173/google&response_type=code&client_id=964104040211-ot7b49o0ih2latnasg1dovkt00dru3u7.apps.googleusercontent.com&scope=https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fuserinfo.email+https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fuserinfo.profile+openid&access_type=offline`
        : `https://github.com/login/oauth/authorize?client_id=Ov23liJnBVkehW1sUsf2&redirect_uri=${githubRedirectUri}&scope=user`;

    console.log('authUrl', authUrl);
    window.location.href = authUrl;
  };

  return (
    <div className="min-h-screen flex items-center justify-center bg-gray-50">
      <Card className="w-full max-w-md p-8">
        <div className="text-center mb-8">
          <h1 className="text-3xl font-bold text-gray-900">Welcome back</h1>
          <p className="text-gray-500 mt-2">Sign in to access your account</p>
        </div>

        <div className="space-y-6">
          <div className="grid grid-cols-2 gap-4">
            <button
              onClick={() => initiateOAuth('google')}
              className="flex items-center justify-center px-4 py-2 border border-gray-300 rounded-md shadow-sm bg-white text-sm font-medium text-gray-700 hover:bg-gray-50"
            >
              <img src="/google.svg" alt="Google" className="h-5 w-5 mr-2" />
              Continue with Google
            </button>
            <button
              onClick={() => initiateOAuth('github')}
              className="flex items-center justify-center px-4 py-2 border border-gray-300 rounded-md shadow-sm bg-white text-sm font-medium text-gray-700 hover:bg-gray-50"
            >
              <img src="/github.svg" alt="GitHub" className="h-5 w-5 mr-2" />
              Continue with GitHub
            </button>
          </div>

          <div className="relative">
            <div className="absolute inset-0 flex items-center">
              <div className="w-full border-t border-gray-300" />
            </div>
            <div className="relative flex justify-center text-sm">
              <span className="px-2 bg-white text-gray-500">Or continue with</span>
            </div>
          </div>

          <form ref={formRef} onSubmit={handleSubmit}>
            <div className="space-y-4">
              <input
                type="email"
                className="block w-full border border-gray-300 rounded-md px-4 py-2 text-sm focus:outline-none focus:ring-1 focus:ring-blue-500"
                placeholder="Email"
                required
              />
              <input
                type="password"
                className="block w-full border border-gray-300 rounded-md px-4 py-2 text-sm focus:outline-none focus:ring-1 focus:ring-blue-500"
                placeholder="Password"
                required
              />
              {errorMessage && <p className="text-sm text-red-500">{errorMessage}</p>}
              <button
                type="submit"
                className="w-full bg-blue-600 text-white py-2 px-4 rounded-md text-sm hover:bg-blue-700"
              >
                Sign in
              </button>
            </div>
          </form>
          <RoleSelectionDialog
            isOpen={showRoleDialog}
            onClose={() => {
              setShowRoleDialog(false);
              setPendingAdminUser(null);
            }}
            onSelect={handleRoleSelection}
          />

          <div className="mt-8">
            <p className="text-center text-sm text-gray-500">
              Don’t have an account?{' '}
              <a href="/register" className="font-medium text-blue-600 hover:text-blue-500">
                Sign up
              </a>
            </p>
          </div>
        </div>
      </Card>
    </div>
  );
}
