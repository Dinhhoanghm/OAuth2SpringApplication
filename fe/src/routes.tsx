import { Routes, Route } from 'react-router-dom';
import LoginPage from '@/pages/login';
import DashboardPage from '@/pages/dashboard';
import SubscriptionsPage from '@/pages/subscriptions';
import ServersPage from '@/pages/servers';
import SettingsPage from '@/pages/settings';
import ClientsListPage from '@/pages/clients-list';
import { ProtectedRoute } from '@/components/protected-route';
import { AppLayout } from '@/components/layout/app-layout';
import { useAuthStore } from '@/lib/store';
import { useEffect } from 'react';
import RegisterPage from './pages/register';
import IntegratedCheckout from './pages/integrateCheckout';
import Success from './pages/successFulPayment';
import Failure from './pages/failurePayment';

export default function AppRoutes() {
  const { user } = useAuthStore();

  // Redirect user role from root route
  useEffect(() => {
    if (user?.role === 'user') {
      window.location.replace('http://localhost:8080/aiv');
    }
  }, [user]);

  return (
    <Routes>
      <Route path="/" element={<LoginPage />} />
      <Route path="/login" element={<LoginPage />} />
      <Route path="/success" element={<Success />} />
      <Route path="/cancel" element={<Failure />} />

      <Route path="/google" element={<LoginPage />} />
      <Route path="/github" element={<LoginPage />} />
      <Route path="/register" element={<RegisterPage />} />
      {/* Super Admin Routes */}
      <Route
        path="/clients"
        element={
          <ProtectedRoute allowedRoles={['super_admin']}>
            <AppLayout>
              <ClientsListPage />
            </AppLayout>
          </ProtectedRoute>
        }
      />
      {/* Admin Routes */}
      <Route
        path="/dashboard"
        element={
          <ProtectedRoute allowedRoles={['admin']}>
            <AppLayout>
              <DashboardPage />
            </AppLayout>
          </ProtectedRoute>
        }
      />
      <Route
        path="/subscriptions"
        element={
          <ProtectedRoute allowedRoles={['admin']}>
            <AppLayout>
              <SubscriptionsPage />
            </AppLayout>
          </ProtectedRoute>
        }
      />
      <Route
        path="/servers"
        element={
          <ProtectedRoute allowedRoles={['admin']}>
            <AppLayout>
              <ServersPage />
            </AppLayout>
          </ProtectedRoute>
        }
      />
      <Route
        path="/settings"
        element={
          <ProtectedRoute allowedRoles={['admin']}>
            <AppLayout>
              <SettingsPage />
            </AppLayout>
          </ProtectedRoute>
        }
      />
    </Routes>
  );
}
