import { Loader2 } from 'lucide-react';

export default function LoadingSpinner() {
  return (
    <div className="flex h-screen items-center justify-center">
      <Loader2 className="h-8 w-8 animate-spin text-gray-500" />
    </div>
  );
}