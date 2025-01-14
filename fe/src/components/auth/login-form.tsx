import { useState, forwardRef, useImperativeHandle, useRef } from 'react';
import { useNavigate } from 'react-router-dom';
import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';
import { Label } from '@/components/ui/label';
import { useAuthStore } from '@/lib/store';
import type { UserRole } from '@/types';
import { RoleSelectionDialog } from './role-selection-dialog';

interface LoginFormHandle {
  setCredentials: (email: string, password: string) => void;
}

const LoginForm = forwardRef<LoginFormHandle>((props, ref) => {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [showRoleDialog, setShowRoleDialog] = useState(false);
  const [pendingAdminUser, setPendingAdminUser] = useState<any>(null);
  const { login } = useAuthStore();
  const navigate = useNavigate();

  useImperativeHandle(ref, () => ({
    setCredentials: (newEmail: string, password: string) => {
      // Handle user role immediately
      if (newEmail === 'user@example.com' && password === 'password') {
        window.location.href = 'http://localhost:8080/aiv';
        return;
      }

      // Handle super admin
      if (newEmail === 'super@example.com' && password === 'password') {
        const user = {
          id: '1',
          email: newEmail,
          role: 'super_admin' as UserRole,
        };
        login(user);
        navigate('/clients', { replace: true });
        return;
      }

      // Handle admin
      if (newEmail === 'admin@example.com' && password === 'password') {
        const user = {
          id: '1',
          email: newEmail,
          role: 'admin' as UserRole,
        };
        setPendingAdminUser(user);
        setShowRoleDialog(true);
        return;
      }
    },
  }));

  const handleRoleSelection = (choice: 'aiv' | 'subscription') => {
    setShowRoleDialog(false);

    if (choice === 'aiv') {
      window.location.href = 'http://localhost:8080/aiv';
    } else {
      if (pendingAdminUser) {
        login(pendingAdminUser);
        navigate('/dashboard', { replace: true });
      }
    }
    setPendingAdminUser(null);
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    // Immediately handle user account without any other processing
    if (email === 'user@example.com' && password === 'password') {
      window.location.href = 'http://localhost:8080/aiv';
      return;
    }

    const testAccounts = {
      admin: { email: 'admin@example.com', password: 'password', role: 'admin' as UserRole },
      super: { email: 'super@example.com', password: 'password', role: 'super_admin' as UserRole },
    };

    const account = Object.values(testAccounts).find((acc) => acc.email === email && acc.password === password);

    if (!account) {
      alert('Invalid credentials. Please use one of the test accounts listed below.');
      return;
    }

    try {
      const user = {
        id: '1',
        email: account.email,
        role: account.role,
      };

      // For admin users, show role selection dialog
      if (account.role === 'admin') {
        setPendingAdminUser(user);
        setShowRoleDialog(true);
        return;
      }

      // For super admin, proceed normally
      login(user);
      navigate('/clients', { replace: true });
    } catch (error) {
      console.error('Login error:', error);
      alert('An error occurred during login. Please try again.');
    }
  };

  return (
    <>
      <form onSubmit={handleSubmit} className="space-y-4">
        <div className="space-y-2">
          <Label htmlFor="email">Email address</Label>
          <Input id="email" type="email" value={email} onChange={(e) => setEmail(e.target.value)} required />
        </div>
        <div className="space-y-2">
          <Label htmlFor="password">Password</Label>
          <Input
            id="password"
            type="password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            required
          />
        </div>
        <Button type="submit" className="w-full">
          Sign in
        </Button>
      </form>

      <RoleSelectionDialog
        isOpen={showRoleDialog}
        onClose={() => {
          setShowRoleDialog(false);
          setPendingAdminUser(null);
        }}
        onSelect={handleRoleSelection}
      />
    </>
  );
});

LoginForm.displayName = 'LoginForm';

export default LoginForm;
