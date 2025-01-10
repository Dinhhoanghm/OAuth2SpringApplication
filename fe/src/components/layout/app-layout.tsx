import { useAuthStore } from '@/lib/store';
import { Link } from 'react-router-dom';
import { LayoutDashboard, Users, Server, Settings, CreditCard, User } from 'lucide-react';

const navigationConfig = {
  super_admin: [{ name: 'Client Management', href: '/clients', icon: Users }],
  admin: [
    { name: 'Dashboard', href: '/dashboard', icon: LayoutDashboard },
    { name: 'Subscriptions', href: '/subscriptions', icon: CreditCard },
    { name: 'Servers', href: '/servers', icon: Server },
    { name: 'Settings', href: '/settings', icon: Settings },
  ],
  user: [
    { name: 'Dashboard', href: '/dashboard', icon: LayoutDashboard },
    { name: 'Subscriptions', href: '/subscriptions', icon: CreditCard },
    { name: 'Settings', href: '/settings', icon: Settings },
  ],
};

export function AppLayout({ children }: { children: React.ReactNode }) {
  const { user } = useAuthStore();

  // Get navigation items based on user role
  const navigationItems = user ? navigationConfig[user.role] : [];

  return (
    <div className="min-h-screen flex">
      {/* Sidebar */}
      <div className="w-64 bg-white border-r">
        <div className="h-16 flex items-center px-6 border-b">
          <h1 className="text-xl font-semibold">
            {user?.role === 'super_admin' ? 'Client Management' : 'Subscription Manager'}
          </h1>
        </div>

        {/* User Info Section */}
        <div className="px-6 py-4 border-b">
          <div className="flex items-center space-x-3">
            <div className="bg-gray-100 p-2 rounded-full">
              <User className="h-5 w-5 text-gray-600" />
            </div>
            <div>
              <div className="text-sm font-medium">{user?.email}</div>
              <div className="text-xs text-gray-500 capitalize">
                {user?.role === 'super_admin' ? 'Super Admin' : user?.role}
              </div>
            </div>
          </div>
        </div>

        <nav className="p-4 space-y-2">
          {navigationItems.map((item) => (
            <Link
              key={item.href}
              to={item.href}
              className="flex items-center space-x-2 px-4 py-2 rounded-md hover:bg-gray-100"
            >
              <item.icon className="h-5 w-5" />
              <span>{item.name}</span>
            </Link>
          ))}
        </nav>

        <div className="absolute bottom-4 left-4">
          <Link
            to="/login"
            className="text-red-600 hover:text-red-700"
            onClick={() => useAuthStore.getState().logout()}
          >
            Logout
          </Link>
        </div>
      </div>

      {/* Main content */}
      <div className="flex-1 overflow-auto">
        {/* Header with user info for mobile */}
        <div className="bg-white border-b p-4 md:hidden">
          <div className="flex items-center justify-between">
            <h1 className="text-xl font-semibold">
              {user?.role === 'super_admin' ? 'Client Management' : 'Subscription Manager'}
            </h1>
            <div className="flex items-center space-x-3">
              <div className="text-sm font-medium">{user?.email}</div>
              <div className="bg-gray-100 p-2 rounded-full">
                <User className="h-5 w-5 text-gray-600" />
              </div>
            </div>
          </div>
        </div>

        <div className="p-8">{children}</div>
      </div>
    </div>
  );
}
