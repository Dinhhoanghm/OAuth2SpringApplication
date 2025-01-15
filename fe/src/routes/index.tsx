import { Routes, Route, Navigate } from 'react-router-dom';
import Layout from '@/components/layout';
import LoginPage from '@/pages/login';
import Dashboard from '@/pages/dashboard';
import Subscriptions from '@/pages/subscriptions';
import Servers from '@/pages/servers';
import Settings from '@/pages/settings';
import ProtectedRoute from '@/components/protected-route';
import RegisterPage from '../pages/register';
import { Elements } from '@stripe/react-stripe-js';
import IntegratedCheckout from '../pages/integrateCheckout';
import Success from '../pages/successFulPayment';
import Failure from '../pages/failurePayment';

export default function AppRoutes() {
  return (
    <Routes>
      <Route path="/login" element={<LoginPage />} />
      <Route path="/success" element={<Success />} />
      <Route path="/cancel" element={<Failure />} />
      <Route path="/google" element={<LoginPage />} />
      <Route path="/github" element={<LoginPage />} />
      <Route path="/register" element={<RegisterPage />} />
      <Route path="/" element={<Layout />}>
        <Route element={<ProtectedRoute />}>
          <Route index element={<Navigate to="/subscriptions" replace />} />
          <Route path="subscriptions" element={<Subscriptions />} />
          <Route path="servers" element={<Servers />} />
          <Route path="settings" element={<Settings />} />
        </Route>
      </Route>
    </Routes>
  );
}
