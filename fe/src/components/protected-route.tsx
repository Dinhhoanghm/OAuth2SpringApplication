import { Navigate } from 'react-router-dom';
import { useAuthStore } from '@/lib/store';
import type { UserRole } from '@/types';
import { useEffect } from 'react';

interface ProtectedRouteProps {
  children: React.ReactNode;
  allowedRoles: UserRole[];
}

export function ProtectedRoute({ children, allowedRoles }: ProtectedRouteProps) {
  const { isAuthenticated, user } = useAuthStore();

  useEffect(() => {
    // Immediately redirect user role to marketplace
    if (user?.role === 'user') {
      window.location.replace('http://localhost:8080/aiv');
    }
  }, [user]);

  if (!isAuthenticated || !user) {
    return <Navigate to="/login" replace />;
  }

  // Double-check user role
  if (user.role === 'user') {
    window.location.replace('http://localhost:8080/aiv');
    return null;
  }

  if (!allowedRoles.includes(user.role)) {
    if (user.role === 'super_admin') {
      return <Navigate to="/clients" replace />;
    }
    return <Navigate to="/dashboard" replace />;
  }

  return <>{children}</>;
}
