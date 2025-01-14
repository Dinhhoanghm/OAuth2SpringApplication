import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from '@/components/ui/table';
import { Button } from '@/components/ui/button';
import { useAuthStore } from '@/lib/store';

interface Client {
  id: string;
  name: string;
  status: 'active' | 'inactive';
  lastActive: string;
  email: string;
  subscriptionType: string;
}

export default function ClientsListPage() {
  const navigate = useNavigate();
  const { logout } = useAuthStore();
  const [clients, setClients] = useState<Client[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    // Temporary dummy data
    const dummyData: Client[] = [
      {
        id: '1',
        name: 'Acme Corporation',
        email: 'contact@acme.com',
        status: 'active',
        subscriptionType: 'Enterprise',
        lastActive: new Date().toISOString(),
      },
      {
        id: '2',
        name: 'TechStart Inc',
        email: 'admin@techstart.com',
        status: 'inactive',
        subscriptionType: 'Professional',
        lastActive: new Date(Date.now() - 86400000).toISOString(),
      },
      {
        id: '3',
        name: 'Global Solutions',
        email: 'info@globalsolutions.com',
        status: 'active',
        subscriptionType: 'Enterprise',
        lastActive: new Date().toISOString(),
      },
    ];

    setTimeout(() => {
      setClients(dummyData);
      setLoading(false);
    }, 500);
  }, []);

  const handleLogout = () => {
    logout();
    localStorage.clear();
    sessionStorage.clear();
    document.cookie.split(";").forEach((c) => {
      document.cookie = c
        .replace(/^ +/, "")
        .replace(/=.*/, "=;expires=" + new Date().toUTCString() + ";path=/");
    });
    navigate('/login');
  };

  if (loading) {
    return (
      <div className="min-h-screen flex items-center justify-center">
        <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-gray-900" />
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-gray-50">
      {/* Header */}
      <header className="bg-white shadow">
        <div className="container mx-auto px-4 py-4 flex justify-between items-center">
          <h1 className="text-2xl font-bold text-gray-900">Client Management</h1>
          <Button variant="ghost" onClick={handleLogout}>
            Logout
          </Button>
        </div>
      </header>

      {/* Main content */}
      <main className="container mx-auto px-4 py-8">
        <div className="bg-white rounded-lg shadow">
          <Table>
            <TableHeader>
              <TableRow>
                <TableHead>Client Name</TableHead>
                <TableHead>Email</TableHead>
                <TableHead>Subscription</TableHead>
                <TableHead>Status</TableHead>
                <TableHead>Last Active</TableHead>
              </TableRow>
            </TableHeader>
            <TableBody>
              {clients.map((client) => (
                <TableRow key={client.id}>
                  <TableCell className="font-medium">{client.name}</TableCell>
                  <TableCell>{client.email}</TableCell>
                  <TableCell>{client.subscriptionType}</TableCell>
                  <TableCell>
                    <span
                      className={`inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium ${
                        client.status === 'active'
                          ? 'bg-green-100 text-green-800'
                          : 'bg-red-100 text-red-800'
                      }`}
                    >
                      {client.status}
                    </span>
                  </TableCell>
                  <TableCell>
                    {new Date(client.lastActive).toLocaleDateString()}
                  </TableCell>
                </TableRow>
              ))}
            </TableBody>
          </Table>
        </div>
      </main>
    </div>
  );
} 